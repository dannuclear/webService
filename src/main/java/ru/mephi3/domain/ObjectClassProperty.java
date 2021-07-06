package ru.mephi3.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OBJECT_CLASS_PROPERTY")
@EqualsAndHashCode(of = "id")
public class ObjectClassProperty {
    @Id
    @Column(name = "ID")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ID_PROPERTY", referencedColumnName = "ID")
    @NotNull(message = "Параметр должен быть задан")
    private Property property;
    @Column(name = "STANDARD")
    @NotEmpty(message = "Норматив не может быть пустым")
    private String standard;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_OBJECT_CLASS", referencedColumnName = "ID")
    private ObjectClass objectClass;
    @JsonIgnore
    @OneToMany(mappedBy = "objectClassProperty", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ObjectClassPropertyDocument> documents;
}
