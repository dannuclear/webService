package ru.mephi3.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import ru.mephi3.domain.Equipment;
import ru.mephi3.domain.EquipmentMaintenance;
import ru.mephi3.domain.EquipmentType;
import ru.mephi3.domain.EquipmentVerification;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class EquipmentDTO {

    private Integer id;
    @NotEmpty
    private String name;
    private String series;
    private String inventory;
    private String producerName;
    private String comment;
    @NotNull
    private EquipmentType equipmentType;
    private List<EquipmentVerification> verifications;
    private List<EquipmentMaintenance> maintenances;
    private EquipmentVerification verification;
    private EquipmentMaintenance maintenance;

    public static EquipmentDTO fromDomain(Equipment equipment) {
        EquipmentDTO result = EquipmentDTO.builder()//
                .id(equipment.getId())//
                .name(equipment.getName())//
                .series(equipment.getSeries())//
                .inventory(equipment.getInventory())//
                .producerName(equipment.getProducerName())//
                .comment(equipment.getComment())//
                .equipmentType(equipment.getEquipmentType())//
                .verifications(equipment.getVerifications())//
                .maintenances(equipment.getMaintenances())//
                .maintenance(equipment.getMaintenance())//
                .verification(equipment.getVerification())//
                .build();
        return result;
    }

    public Equipment toDomain() {
        final Equipment equipment = Equipment.builder()//
                .id(id)//
                .name(name)//
                .series(series)//
                .inventory(inventory)//
                .producerName(producerName)//
                .comment(comment)//
                .equipmentType(equipmentType)//
                .verifications(verifications)//
                .maintenances(maintenances)//
                .maintenance(maintenance)//
                .verification(verification)//
                .build();
        if (!CollectionUtils.isEmpty(verifications))
            verifications.stream().forEach(v -> v.setEquipment(equipment));
        if (!CollectionUtils.isEmpty(maintenances))
            maintenances.stream().forEach(v -> v.setEquipment(equipment));
        return equipment;
    }

    public static EquipmentDTO createDefault() {
        return EquipmentDTO.builder().verifications(new ArrayList<>()).maintenances(new ArrayList<>()).build();
    }
}
