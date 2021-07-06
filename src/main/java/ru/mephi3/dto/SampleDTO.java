package ru.mephi3.dto;

import lombok.*;
import ru.mephi3.domain.Sample;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SampleDTO {
    private Integer id;
    @NotEmpty(message = "Код не может быть пустым")
    private String code;
    @NotEmpty(message = "Наименование пробы не может быть пустым")
    private String name;
    private LocalDateTime selectionDateTime;

    public static SampleDTO fromDomain(Sample domain) {
        return SampleDTO.builder()//
                .id(domain.getId())//
                .code(domain.getCode())//
                .name(domain.getName())//
                .selectionDateTime(domain.getSelectionDateTime())//
                .build();
    }

    public Sample toDomain() {
        return Sample.builder()//
                .id(this.id)//
                .code(this.code)//
                .name(this.name)//
                .selectionDateTime(this.selectionDateTime)//
                .build();
    }

    public static SampleDTO createDefault() {
        return SampleDTO.builder().build();
    }
}
