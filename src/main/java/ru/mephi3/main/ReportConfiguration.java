package ru.mephi3.main;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "reports")
public class ReportConfiguration {
    private static final String DEFAULT_LOCATION = "/WEB-INF/reports/";
    private static final String DEFAULT_SOURCE_LOCATION = DEFAULT_LOCATION + "source";
    private static final String DEFAULT_COMPILED_LOCATION = DEFAULT_LOCATION + "compiled";

    private String sourceLocation = DEFAULT_SOURCE_LOCATION;
    private String compiledLocation = DEFAULT_COMPILED_LOCATION;
}
