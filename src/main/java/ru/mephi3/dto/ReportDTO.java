package ru.mephi3.dto;

import lombok.*;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import ru.mephi3.domain.Report;
import ru.mephi3.utils.ReportUtils;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReportDTO {
    private Integer id;
    @NotEmpty(message = "Имя не может быть пустым")
    private String name;
    private MultipartFile data;
    private String fileName;
    private List<Parameter> params;
    private String sourceTempFile;

    public static ReportDTO fromDomain(Report domain) {
        return ReportDTO.builder()//
                .id(domain.getId())//
                .name(domain.getName())//
                .fileName(domain.getFileName())//
                .params(domain.getParams() == null ? Collections.emptyList() : domain.getParams().entrySet().stream().map(Parameter::of).collect(Collectors.toList()))
                .build();
    }

    public Report toDomain() {
        return Report.builder()//
                .id(this.id)//
                .name(this.name)//
                .fileName(hasData() ? this.data.getOriginalFilename() : this.fileName)//
                .params(params.stream().collect(HashMap::new, (m, v) -> m.put(v.getName(), v.getValue()), HashMap::putAll))
                .build();
    }

    public static ReportDTO createDefault() {
        return ReportDTO.builder().params(new ArrayList<>()).build();
    }

    public void fillParameters(JasperReport jr) {
        if (jr == null)
            return;
        this.params = Arrays.stream(jr.getParameters())
                .filter(ReportUtils::excludeParameter)
                .map(Parameter::of)
                .peek(p -> {
                    int i = this.params.indexOf(p);
                    if (i == -1)
                        return;
                    Object val = this.params.get(i).getValue();
                    if (val != null && (val.getClass().getName().compareTo(p.getClassName())) == 0)
                        p.setValue(val);
                })
                .collect(Collectors.toList());
    }

    public boolean hasData() {
        return this.data != null && !StringUtils.isEmpty(this.data.getOriginalFilename()) && !this.data.isEmpty();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(of = "name")
    public static class Parameter {
        private String name;
        private String className;
        private Object value;

        public Parameter(String name, String className) {
            this.name = name;
            this.className = className;
        }

        public static Parameter of(String name, String className) {
            return new Parameter(name, className);
        }

        public static Parameter of(Map.Entry<String, Object> e) {
            Object value = e.getValue();
            Parameter p = of(e.getKey(), null);
            if (value != null)
                p.setClassName(value.getClass().getName());
            p.setValue(e.getValue());
            return p;
        }

        public static Parameter of(JRParameter p) {
            return of(p.getName(), p.getValueClassName());
        }

        public Boolean getBoolean() {
            return value != null ? (Boolean) value : Boolean.TRUE;
        }

        public void setBoolean(Boolean v) {
            this.value = v;
        }

        public String getString() {
            return (String) this.value;
        }

        public void setString(String v) {
            this.value = v;
        }

        public Double getDouble() {
            return (Double) this.value;
        }

        public void setDouble(Double v) {
            this.value = v;
        }

        public Integer getInteger() {
            return (Integer) this.value;
        }

        public void setInteger(Integer v) {
            this.value = v;
        }
    }
}
