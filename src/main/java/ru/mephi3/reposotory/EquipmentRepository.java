package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi3.domain.Equipment;
import ru.mephi3.domain.EquipmentMaintenance;
import ru.mephi3.domain.EquipmentVerification;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    @EntityGraph(attributePaths = {"equipmentType", "maintenance", "verification"})
    Page<Equipment> findByNameContainsIgnoreCase(String part, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"equipmentType", "maintenance", "verification"})
//    @Query("SELECT e FROM Equipment e")
    Page<Equipment> findAll(Pageable pageable);

    List<Equipment> findByIdIn(List<Integer> ids);

    @Override
    @EntityGraph(attributePaths = {"equipmentType", "maintenance", "verification"})
    Optional<Equipment> findById(Integer integer);

    @Modifying
    @Query("UPDATE Equipment e SET e.maintenance = ?2, e.verification = ?3 WHERE e = ?1")
    void updateCurrentMaintenanceAndVerification(Equipment equipment, EquipmentMaintenance equipmentMaintenance, EquipmentVerification equipmentVerification);
}