package ru.mephi3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "OBJECT_CLASS_PROPERTY_DOCUMENT")
public class ObjectClassPropertyDocument {
    @Id
    @Column(name = "ID")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ID_DOCUMENT_TYPE", referencedColumnName = "ID")
    private DocumentType type;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_OBJECT_CLASS_PROPERTY", referencedColumnName = "ID")
    private ObjectClassProperty objectClassProperty;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_DOCUMENT", referencedColumnName = "ID")
    private Document document;
}
