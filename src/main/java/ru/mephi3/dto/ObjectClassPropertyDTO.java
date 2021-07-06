package ru.mephi3.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.mephi3.domain.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class ObjectClassPropertyDTO {
    private Integer id;
    @NotNull(message = "Параметр должен быть задан")
    private Integer propertyId;
    private String propertyName;
    @NotEmpty(message = "Норматив не может быть пустым")
    private String standard;
    @NotNull(message = "Не задан класс объекта")
    private Integer objectClassId;
    private String objectClassTree;
    @JsonIgnore
    private Map<DocumentType, List<ObjectClassPropertyDocument>> documentsByType;

    public static ObjectClassPropertyDTO fromDomain(ObjectClassProperty objectClassProperty) {
        return ObjectClassPropertyDTO.builder()//
                .id(objectClassProperty.getId())//
                .propertyId(objectClassProperty.getProperty().getId())//
                .propertyName(objectClassProperty.getProperty().getName())//
                .standard(objectClassProperty.getStandard())//
                .objectClassId(objectClassProperty.getObjectClass().getId())//
                .documentsByType(createDocumentMapFromList(objectClassProperty.getDocuments())) //
                .build();
    }

    public ObjectClassProperty toDomain() {
        Property property = Property.builder().id(propertyId).build();
        ObjectClass objectClass = ObjectClass.builder().id(objectClassId).build();
        return ObjectClassProperty.builder()//
                .id(id)//
                .property(property)//
                .standard(standard)//
                .objectClass(objectClass)//
                .documents(createDocumentListFromMap(documentsByType)) //
                .build();
    }

    public static ObjectClassPropertyDTO createDefault() {
        return ObjectClassPropertyDTO.builder().documentsByType(Collections.emptyMap()).build();
    }

    public static Map<DocumentType, List<ObjectClassPropertyDocument>> createDocumentMapFromList(List<ObjectClassPropertyDocument> documents) {
        return documents.stream().collect(Collectors.groupingBy(ObjectClassPropertyDocument::getType));
    }

    public static List<ObjectClassPropertyDocument> createDocumentListFromMap(Map<DocumentType, List<ObjectClassPropertyDocument>> documentMap) {
        return documentMap.entrySet().stream().map(Map.Entry::getValue).flatMap(List::stream).collect(Collectors.toList());
    }
}
