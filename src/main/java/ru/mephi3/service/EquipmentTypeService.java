package ru.mephi3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mephi3.domain.EquipmentType;

import java.util.List;
import java.util.Optional;

public interface EquipmentTypeService {

	EquipmentType create(String code);

	EquipmentType save(EquipmentType equipmentType);

	Optional<EquipmentType> findById(Integer id);

    Page<EquipmentType> findByFilter(String search, Pageable pageable);

    Page<EquipmentType> findAll(Pageable pageable);

	Page<EquipmentType> findByString(String value, Pageable pageable);

	List<EquipmentType> findByIds(List<Integer> ids);

	void delete(EquipmentType equipmentType);
}
