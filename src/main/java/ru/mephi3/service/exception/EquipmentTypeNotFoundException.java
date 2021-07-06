package ru.mephi3.service.exception;

public class EquipmentTypeNotFoundException extends RuntimeException {

	public EquipmentTypeNotFoundException(Integer id) {
		super("тип оборудование не найдено с ID: " + id);
	}
}
