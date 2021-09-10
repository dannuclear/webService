package ru.mephi3.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.mephi3.domain.Report;
import ru.mephi3.main.ReportConfiguration;
import ru.mephi3.reposotory.ReportRepository;
import ru.mephi3.service.ReportService;
import ru.mephi3.service.exception.ReportCompileException;
import ru.mephi3.service.exception.ReportFillException;
import ru.mephi3.service.exception.ReportIOException;
import ru.mephi3.utils.ReportUtils;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ApplicationContext applicationContext;
    private final ReportConfiguration reportConfiguration;
    private final DataSource dataSource;

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public Report save(Report report) {
        Report res = reportRepository.save(report);
        return res;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public Report create(String code) {
        Report report = new Report();
        return report;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasRole('ADMIN')")
    public Optional<Report> findById(Integer id) {
        Optional<Report> optReport = reportRepository.findById(id);
        return optReport;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasRole('ADMIN')")
    public InputStream getSourceStream(Report report) throws ReportIOException {
        if (report == null || StringUtils.isEmpty(report.getFileName()))
            throw new IllegalArgumentException("Отчет и имя файла должны быть заданы");
        Resource resource = getSourceResource(report.getFileName());
        if (!resource.exists())
            throw new ReportIOException("Файл отчета не существует");
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new ReportIOException("Ошибка чтения отчета", e);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasAnyRole('ADMIN')")
    public Page<Report> findAll(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public void delete(Report report) {
        reportRepository.delete(report);
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasAnyRole('ADMIN')")
    public Page<Report> findByString(String value, Pageable pageable) {
        return reportRepository.findByNameContainsIgnoreCase(value, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasAnyRole('ADMIN')")
    public Page<Report> findByStringNoLob(String value, Pageable pageable) {
        return reportRepository.findByStringNoLob(value, pageable);
    }


    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public void saveSourceToFile(InputStream inputStream, String fileName) throws ReportIOException {
        Resource reportResource = getSourceResource(fileName);
        try {
            File file = reportResource.getFile();
            if (!file.exists())
                file.createNewFile();
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ReportIOException(String.format("Не могу сохранить исходник отчета: %s, %s", fileName, e.getMessage()));
        }
    }

    @Override
    public Path saveToTempFile(InputStream is) throws ReportIOException {
        Path tmpFile = null;
        try {
            tmpFile = Files.createTempFile(null, null);
            Files.deleteIfExists(tmpFile);
            Files.copy(is, tmpFile);
        } catch (IOException e) {
            throw new ReportIOException(String.format("Не могу сохранить исходник отчета во временный файл: %s", e.getMessage()), e);
        }
        return tmpFile;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public void saveSourceAndCompile(InputStream inputStream, String fileName) throws ReportIOException, ReportCompileException {
        saveSourceToFile(inputStream, fileName);
        compileToFile(fileName);
    }

    @Override
    public JasperReport getJasperReport(String fileName) throws ReportIOException {
        Resource compiled = getCompiledResource(fileName);
        try {
            return (JasperReport) JRLoader.loadObject(compiled.getInputStream());
        } catch (IOException | JRException e) {
            throw new ReportIOException("Не могу сформировать отчет: " + fileName, e);
        }
    }

    @Override
    public JasperPrint fillReport(Report report, Map<String, Object> extraParam) throws ReportFillException {
        String fileName = report.getFileName();
        if (StringUtils.isEmpty(fileName))
            throw new ReportFillException("Ошибка при заполнении отчета " + fileName + ". Имя файла не задано");
        try (Connection connection = dataSource.getConnection()) {
            JasperReport jr = getJasperReport(fileName);
            Map<String, Object> params = new HashMap<>();

            if (report.getParams() != null)
                params.putAll(report.getParams());
            if (extraParam != null)
                params.putAll(extraParam);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, connection);
            return jp;
        } catch (JRException | SQLException | ReportIOException e) {
            throw new ReportFillException("Ошибка при заполнении отчета " + fileName, e);
        }
    }

    @Override
    public boolean existsSource(final String fileName) {
        if (fileName == null && fileName.length() == 0)
            throw new IllegalArgumentException("Файл не задан");
        String fn = FilenameUtils.removeExtension(fileName) + ".jrxml";
        return getSourceResource(fn).exists();
    }

    @Override
    public boolean existsCompiled(String fileName) {
        if (fileName == null && fileName.length() == 0)
            throw new IllegalArgumentException("Файл не задан");
        String fn = FilenameUtils.removeExtension(fileName) + ".jasper";
        return getCompiledResource(fn).exists();
    }

    private void compileToFile(String sourceFileName) throws ReportCompileException {
        try {
            Resource compiledResource = getCompiledResource(sourceFileName);
            File compiledFile = compiledResource.getFile();
            JasperReport jr = compile(sourceFileName);
            JRSaver.saveObject(jr, compiledFile.getPath());
        } catch (JRException e) {
            throw new ReportCompileException(String.format("Не могу сохранить скомпилированный отчет: %s, %s", sourceFileName, e.getMessage()), e);
        } catch (IOException e) {
            throw new ReportCompileException(String.format("Ошибка чтения/записи файла отчета при компиляции: %s, %s", sourceFileName, e.getMessage()), e);
        }
    }

    @Override
    public JasperReport compile(String sourceFileName) throws ReportCompileException {
        try {
            File sourceFile = getSourceResource(sourceFileName).getFile();
            return JasperCompileManager.compileReport(sourceFile.getPath());
        } catch (JRException | IOException e) {
            throw new ReportCompileException(String.format("Не могу скомпилировать отчет: %s, %s", sourceFileName, e.getMessage()), e);
        }
    }

    @Override
    public JasperReport compile(InputStream inputStream) throws ReportCompileException {
        try {
            return JasperCompileManager.compileReport(inputStream);
        } catch (JRException e) {
            throw new ReportCompileException(String.format("Не могу скомпилировать отчет: %s", e.getMessage()), e);
        }
    }

    @Override
    public Resource getSourceResource(String fileName) {
        String path = getSourceRelativePath(fileName);
        return applicationContext.getResource(path);
    }

    @Override
    public Resource getCompiledResource(String fileName) {
        String path = getCompiledRelativePath(fileName);
        return applicationContext.getResource(path);
    }

    private String getSourceRelativePath(String fileName) {
        String sourceLocation = reportConfiguration.getSourceLocation();
        return (sourceLocation.endsWith("/") ? sourceLocation : sourceLocation + '/') + FilenameUtils.removeExtension(fileName) + ".jrxml";
    }

    private String getCompiledRelativePath(String fileName) {
        String compiledLocation = reportConfiguration.getCompiledLocation();
        return (compiledLocation.endsWith("/") ? compiledLocation : compiledLocation + '/') + FilenameUtils.removeExtension(fileName) + ".jasper";
    }
}
