package ru.mephi3.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EquipmentNotFoundException extends RuntimeException {

	public EquipmentNotFoundException(Integer id) {
		super("Оборудование не найдено с ID: " + id);
	}
}
