package ru.mephi3.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.mephi3.domain.EquipmentType;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class EquipmentTypeDTO {

    private Integer id;
    private String name;

    public static EquipmentTypeDTO fromDomain(EquipmentType equipmentType) {
        return EquipmentTypeDTO.builder()//
                .id(equipmentType.getId())//
                .name(equipmentType.getName())//
                .build();
    }

    public EquipmentType toDomain() {
        return EquipmentType.builder()//
                .id(id)//
                .name(name)//
                .build();
    }

    public static EquipmentTypeDTO createDefault() {
        return EquipmentTypeDTO.builder().build();
    }
}
