package ru.mephi3.dto;

import lombok.*;
import ru.mephi3.domain.Report;

import javax.validation.constraints.NotEmpty;

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

    public static ReportDTO fromDomain(Report domain) {
        return ReportDTO.builder()//
                .id(domain.getId())//
                .name(domain.getName())//
                .build();
    }

    public Report toDomain() {
        return Report.builder()//
                .id(this.id)//
                .name(this.name)//
                .build();
    }

    public static ReportDTO createDefault() {
        return ReportDTO.builder().build();
    }
}
