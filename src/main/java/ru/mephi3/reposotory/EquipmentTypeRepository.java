package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi3.domain.Equipment;
import ru.mephi3.domain.EquipmentType;

import java.util.List;

@Transactional(readOnly = true)
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Integer> {
    Page<EquipmentType> findByNameContainsIgnoreCase(String part, Pageable pageable);

    List<EquipmentType> findByIdIn(List<Integer> ids);
}