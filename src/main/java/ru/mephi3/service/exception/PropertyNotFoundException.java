package ru.mephi3.service.exception;

public class PropertyNotFoundException extends RuntimeException {

	public PropertyNotFoundException(Integer id) {
		super("Показатель не найден с id " + id);
	}
}
