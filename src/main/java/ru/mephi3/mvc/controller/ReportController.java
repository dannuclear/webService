package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.mephi3.dto.ReportDTO;
import ru.mephi3.service.ReportService;
import ru.mephi3.service.exception.ReportNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/private/reports")
@RequiredArgsConstructor
@SessionAttributes({"report"})
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
                report = reportService.findById(id).map(eq -> {
                    ReportDTO reportDTO = ReportDTO.fromDomain(eq);
                    return reportDTO;
                }).orElseThrow(() -> new ReportNotFoundException(id));
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
        mv.setViewName("redirect:" + ALL_VIEW_NAME);
        return mv;
    }

    @PostMapping({"{reportId}", "new"})
    public String save(@PathVariable(required = false) Integer reportId, @ModelAttribute("report") @Valid ReportDTO reportDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }
        if (reportId != null) {
            reportService.findById(reportId).map(report -> {
                Report reportToSave = reportDTO.toDomain();
                reportToSave.setId(report.getId());
                return reportService.save(reportToSave);
            }).orElseThrow(() -> new ReportNotFoundException(reportId));
        } else {
            reportService.save(reportDTO.toDomain());
        }
        sessionStatus.setComplete();
        return REDIRECT_ALL_URL;
    }

    @PostMapping("uploadData")
    public String uploadData (@RequestParam("file") MultipartFile file, Model model){

        return "redirect: #";
    }
}