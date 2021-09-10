package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.Report;
import ru.mephi3.dto.ReportCompileStatusDTO;
import ru.mephi3.dto.ReportDTO;
import ru.mephi3.report.Format;
import ru.mephi3.service.ReportService;
import ru.mephi3.service.exception.ReportCompileException;
import ru.mephi3.service.exception.ReportFillException;
import ru.mephi3.service.exception.ReportIOException;
import ru.mephi3.service.exception.ReportNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/private/reports")
@RequiredArgsConstructor
@SessionAttributes({"report", "parameters"})
@Log4j2
public class ReportController {
    public static final String EDIT_VIEW_NAME = "/private/report/edit";
    public static final String ALL_VIEW_NAME = "/private/report/all";
    public static final String REDIRECT_ALL_URL = "redirect:/private/reports";

    private final ReportService reportService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<Report>> findPageable(@RequestBody DataTablesRequest dataTablesRequest, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        Page<Report> reportPage = reportService.findByString(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        reportPage.getContent().stream().filter(r -> r.getFileName() != null && r.getFileName().length() != 0).forEach(r -> {
            r.setExistsCompiled(reportService.existsCompiled(r.getFileName()));
            r.setExistsSource(reportService.existsSource(r.getFileName()));
        });
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), reportPage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return ALL_VIEW_NAME;
    }

    @GetMapping({"{reportId}", "new"})
    @Transactional(readOnly = true)
    public String edit(@PathVariable(value = "reportId", required = false) Integer id, Model model) {
        if (!model.containsAttribute("report")) {
            ReportDTO report = null;
            if (id != null) {
                Report dbReport = reportService.findById(id).orElseThrow(() -> new ReportNotFoundException(id));
                report = ReportDTO.fromDomain(dbReport);
                if (report.getFileName() != null) {
                    try {
                        JasperReport jr = reportService.getJasperReport(report.getFileName());
                        if (jr != null)
                            report.fillParameters(jr);
                    } catch (ReportIOException e) {

                    }
                }
            } else
                report = ReportDTO.createDefault();
            model.addAttribute("report", report);
        }
        model.addAttribute("readOnly", false);
        return EDIT_VIEW_NAME;
    }

    @GetMapping("/{reportId}/delete")
    public ModelAndView delete(@PathVariable("reportId") Integer reportId, ModelAndView mv) {
        reportService.findById(reportId).map(report -> {
            reportService.delete(report);
            return report;
        }).orElseThrow(() -> new ReportNotFoundException(reportId));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName(REDIRECT_ALL_URL);
        return mv;
    }

    @PostMapping({"{reportId}", "new"})
    public String save(@PathVariable(required = false) Integer reportId, @ModelAttribute("report") @Valid ReportDTO reportDTO, BindingResult bindingResult, SessionStatus sessionStatus) throws Exception {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }
        Report reportToSave = null;
        if (reportId != null) {
            Report report = reportService.findById(reportId).orElseThrow(() -> new ReportNotFoundException(reportId));
            reportToSave = reportDTO.toDomain();
            reportToSave.setId(report.getId());

        } else {
            reportToSave = reportDTO.toDomain();
        }

        if (reportDTO.getSourceTempFile() != null) {
            Path path = Paths.get(reportDTO.getSourceTempFile());
            reportService.saveSourceAndCompile(Files.newInputStream(path), reportDTO.getFileName());
            Files.deleteIfExists(path);
        }
        reportService.save(reportToSave);

        sessionStatus.setComplete();
        return REDIRECT_ALL_URL;
    }

    @GetMapping("{reportId}/source")
    public ResponseEntity<Resource> getSource(@PathVariable("reportId") Integer reportId) {
        Report report = reportService.findById(reportId).orElseThrow(() -> new ReportNotFoundException(reportId));
        Resource resource = reportService.getSourceResource(report.getFileName());
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(mediaType);
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(report.getFileName()).build();
        httpHeaders.setContentDisposition(contentDisposition);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    @GetMapping({"{reportId}/show"})
    public ResponseEntity<Resource> showReport(@PathVariable Integer reportId, @RequestParam Map<String, Object> extraParams, @RequestParam(defaultValue = "HTML") Format format) throws ReportFillException {
        Report report = reportService.findById(reportId).orElseThrow(() -> new ReportNotFoundException(reportId));
        JasperPrint jp = reportService.fillReport(report, extraParams);

        log.info("Формирование HTML отчета: {} с параметрами {}", report.getName(), extraParams);

        HtmlExporter exporter = new HtmlExporter();
        exporter.setExporterInput(new SimpleExporterInput(jp));

        ByteArrayResource resource = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(os));
            exporter.exportReport();
            resource = new ByteArrayResource(os.toByteArray());
        } catch (JRException | IOException e) {
            throw new ReportFillException("Не удалось сформировать отчет", e);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        ContentDisposition contentDisposition = ContentDisposition.inline().filename(report.getFileName()).build();
        httpHeaders.setContentDisposition(contentDisposition);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("upload-source")
    public ResponseEntity<ReportCompileStatusDTO> uploadSource(@RequestParam("data-source") MultipartFile multipartFile, @ModelAttribute("report") ReportDTO reportDTO) {

        if (multipartFile == null || multipartFile.isEmpty())
            return ResponseEntity.ok(ReportCompileStatusDTO.builder().message("Файл не выбран").build());
        Path sourceTemp = null;
        try {
            sourceTemp = Files.createTempFile(null, null);
            multipartFile.transferTo(sourceTemp);
            JasperReport jr = reportService.compile(multipartFile.getInputStream());
            reportDTO.fillParameters(jr);
            reportDTO.setSourceTempFile(sourceTemp.toString());
            reportDTO.setFileName(multipartFile.getOriginalFilename());
        } catch (ReportCompileException | IOException e) {
            try {
                Files.deleteIfExists(sourceTemp);
            } catch (IOException ioException) {
                return ResponseEntity.ok(ReportCompileStatusDTO.builder().message(ioException.getMessage()).build());
            }
            return ResponseEntity.ok(ReportCompileStatusDTO.builder().message(e.getMessage()).build());
        }

        return ResponseEntity.ok(ReportCompileStatusDTO.builder().message("Отчет собран").build());
    }

    @GetMapping("report-properties")
    public String getParameters(Model model) {
        return "fragments/fragments :: reportProperties";
    }
}