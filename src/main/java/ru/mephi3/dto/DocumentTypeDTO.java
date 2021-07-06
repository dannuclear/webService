package ru.mephi3.dto;

import lombok.*;
import ru.mephi3.domain.DocumentType;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeDTO {
    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String shortName;

    public DocumentType toDomain() {
        return DocumentType//
                .builder()//
                .id(id)//
                .name(name)//
                .shortName(shortName)//
                .build();
    }

    public static DocumentTypeDTO fromDomain(DocumentType doc) {
        return DocumentTypeDTO//
                .builder()//
                .id(doc.getId())//
                .name(doc.getName())//
                .shortName(doc.getShortName())//
                .build();
    }

    public static DocumentTypeDTO createDefault() {
        return DocumentTypeDTO.builder().build();
    }
}
