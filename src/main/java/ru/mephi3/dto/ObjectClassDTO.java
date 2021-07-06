package ru.mephi3.dto;

import lombok.*;
import ru.mephi3.domain.ObjectClass;
import ru.mephi3.domain.Sample;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ObjectClassDTO {
    private Integer id;
    private String okpdCode;
    private String name;
    private ObjectClassDTO parent;

    public static ObjectClassDTO fromDomain(ObjectClass domain) {
        return ObjectClassDTO.builder()//
                .id(domain.getId())//
                .name(domain.getName())//
                .build();
    }

    public Sample toDomain() {
        return Sample.builder()//
                .id(this.id)//
                .name(this.name)//
                .build();
    }

    public static ObjectClassDTO createDefault() {
        return ObjectClassDTO.builder().build();
    }
}
