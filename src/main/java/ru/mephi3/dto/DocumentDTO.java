package ru.mephi3.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.mephi3.domain.Document;


import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class DocumentDTO {

    private Integer id;
    @NotEmpty
    private String header;
    private String title;
    private LocalDate commitDate;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Document.Status status;
    private String comment;
    private Integer documentTypeId;
    private String documentTypeName;

    public static DocumentDTO fromDomain(Document document) {
        return DocumentDTO.builder()//
                .id(document.getId())//
                .header(document.getHeader())//
                .title(document.getTitle())//
                .commitDate(document.getCommitDate())//
                .beginDate(document.getBeginDate())//
                .endDate(document.getEndDate())//
                .status(document.getStatus())//
                .comment(document.getComment())//
                .build();
    }

    public Document toDomain() {
        return Document.builder()//
                .id(id)//
                .header(header)//
                .title(title)//
                .commitDate(commitDate)//
                .beginDate(beginDate)//
                .endDate(endDate)//
                .status(status)//
                .comment(comment)//
                .build();
    }

    public static DocumentDTO createDefault() {
        return DocumentDTO.builder().build();
    }
}
