package ru.mephi3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OrderBy;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EQUIPMENT")
@Relation(collectionRelation = "equipments")
public class Equipment {

    @Id
    @Column(name = "ID")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SERIES")
    private String series;
    @Column(name = "INVENTORY")
    private String inventory;
    @Column(name = "PRODUCER_NAME")
    private String producerName;
    @Column(name = "COMMENT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "ID_EQUIPMENT_TYPE", referencedColumnName = "ID")
    private EquipmentType equipmentType;

    @JsonIgnore
    @OneToMany(mappedBy = "equipment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy(clause = "createDate DESC")
    private List<EquipmentVerification> verifications;
    @JsonIgnore
    @OneToMany(mappedBy = "equipment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy(clause = "createDate DESC")
    private List<EquipmentMaintenance> maintenances;

    @ManyToOne
    @JoinColumn(name = "ID_EQUIPMENT_VERIFICATION", insertable = false, updatable = false)
    private EquipmentVerification verification;
    @ManyToOne
    @JoinColumn(name = "ID_EQUIPMENT_MAINTENANCE", insertable = false, updatable = false)
    private EquipmentMaintenance maintenance;
}
