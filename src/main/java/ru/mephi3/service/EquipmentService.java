package ru.mephi3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.domain.Equipment;

import java.util.List;
import java.util.Optional;

public interface EquipmentService {

	Equipment create(String code);

	Equipment save(Equipment equipment);

	Optional<Equipment> findById(Integer id);

    Page<Equipment> findByFilter(String search, Pageable pageable);

    Page<Equipment> findAll(Pageable pageable);

	Page<Equipment> findByString(String value, Pageable pageable);

	List<Equipment> findByIds(List<Integer> ids);

	void delete(Equipment equipment);
}
