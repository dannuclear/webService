package ru.mephi3.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.domain.Equipment;
import ru.mephi3.reposotory.EquipmentRepository;
import ru.mephi3.service.EquipmentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

	private final EquipmentRepository equipmentRepository;

	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_EDIT') or hasAnyRole('ADMIN')")
	public Equipment save(Equipment equipment) {
		Equipment res = equipmentRepository.save(equipment);
		equipmentRepository.updateCurrentMaintenanceAndVerification(equipment, equipment.getMaintenance(), equipment.getVerification());
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_EDIT') or hasAnyRole('ADMIN')")
	public Equipment create(String code) {
		Equipment equipment = new Equipment();
		return equipment;
	}

	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_READ') or hasRole('ADMIN')")
	public Optional<Equipment> findById(Integer id) {
		Optional<Equipment> optEquipment = equipmentRepository.findById(id);
		return optEquipment;
	}

	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_READ') or hasAnyRole('ADMIN')")
	public Page<Equipment> findByFilter(String search, Pageable pageable) {
		return equipmentRepository.findByNameContainsIgnoreCase(search, pageable);
	}


	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_READ') or hasAnyRole('ADMIN')")
	public Page<Equipment> findAll(Pageable pageable) {
		return equipmentRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_EDIT') or hasAnyRole('ADMIN')")
	public void delete(Equipment equipment) {
		equipmentRepository.delete(equipment);
	}

	@Override
	@PreAuthorize("hasAuthority('EQUIPMENT_READ') or hasAnyRole('ADMIN')")
	public Page<Equipment> findByString(String value, Pageable pageable) {
		return null;
	}

	@Override
	public List<Equipment> findByIds(List<Integer> ids) {
		List<Equipment> equipments = equipmentRepository.findByIdIn(ids);
		return equipments;
	}
}
