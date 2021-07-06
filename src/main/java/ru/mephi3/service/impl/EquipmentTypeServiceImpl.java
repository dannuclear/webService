package ru.mephi3.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.mephi3.domain.EquipmentType;
import ru.mephi3.reposotory.EquipmentTypeRepository;
import ru.mephi3.service.EquipmentTypeService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentTypeServiceImpl implements EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_EDIT') or hasAnyRole('ADMIN')")
    public EquipmentType save(EquipmentType equipmentType) {
        EquipmentType res = equipmentTypeRepository.save(equipmentType);
        return res;
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_EDIT') or hasAnyRole('ADMIN')")
    public EquipmentType create(String code) {
        EquipmentType equipmentType = new EquipmentType();
        return equipmentType;
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_READ') or hasRole('ADMIN')")
    public Optional<EquipmentType> findById(Integer id) {
        Optional<EquipmentType> optEquipmentType = equipmentTypeRepository.findById(id);
        return optEquipmentType;
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_READ') or hasAnyRole('ADMIN')")
    public Page<EquipmentType> findByFilter(String search, Pageable pageable) {
        return equipmentTypeRepository.findByNameContainsIgnoreCase(search, pageable);
    }


    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_READ') or hasAnyRole('ADMIN')")
    public Page<EquipmentType> findAll(Pageable pageable) {
        return equipmentTypeRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_EDIT') or hasAnyRole('ADMIN')")
    public void delete(EquipmentType equipmentType) {
        equipmentTypeRepository.delete(equipmentType);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE_READ') or hasAnyRole('ADMIN')")
    public Page<EquipmentType> findByString(String value, Pageable pageable) {
        return null;
    }

    @Override
    public List<EquipmentType> findByIds(List<Integer> ids) {
        List<EquipmentType> equipmentTypes = equipmentTypeRepository.findByIdIn(ids);
        return equipmentTypes;
    }
}
