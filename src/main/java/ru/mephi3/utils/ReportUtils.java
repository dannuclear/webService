package ru.mephi3.utils;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.mephi3.dto.ReportDTO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportUtils {
    public static final Set<String> EXCLUDE_PARAMS = new HashSet<>();

    static {
        EXCLUDE_PARAMS.add(JRParameter.REPORT_PARAMETERS_MAP);
        EXCLUDE_PARAMS.add(JRParameter.JASPER_REPORTS_CONTEXT);
        EXCLUDE_PARAMS.add(JRParameter.JASPER_REPORT);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_CONNECTION);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_MAX_COUNT);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_DATA_SOURCE);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_SCRIPTLET);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_LOCALE);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_RESOURCE_BUNDLE);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_TIME_ZONE);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_VIRTUALIZER);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_CLASS_LOADER);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_FORMAT_FACTORY);
        EXCLUDE_PARAMS.add(JRParameter.IS_IGNORE_PAGINATION);
        EXCLUDE_PARAMS.add(JRParameter.MAX_PAGE_HEIGHT);
        EXCLUDE_PARAMS.add(JRParameter.MAX_PAGE_WIDTH);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_TEMPLATES);
        EXCLUDE_PARAMS.add(JRParameter.SORT_FIELDS);
        EXCLUDE_PARAMS.add(JRParameter.REPORT_CONTEXT);
        EXCLUDE_PARAMS.add(JRParameter.FILTER);
    }

    public static boolean excludeParameter(JRParameter p) {
        return !EXCLUDE_PARAMS.contains(p.getName());
    }

    public static Resource getResource(ResourcePatternResolver pathResolver, String prefix, String fileName) {
        final String path = (prefix.endsWith("/") ? prefix : prefix + '/') + fileName;
        return pathResolver.getResource(path);
    }

    public static Boolean exists(ResourcePatternResolver pathResolver, String prefix, String fileName) {
        return getResource(pathResolver, prefix, fileName).exists();
    }
}
