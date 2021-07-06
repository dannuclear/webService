package ru.mephi3.service.exception;

public class ObjectClassPropertyNotFoundException extends RuntimeException {

	public ObjectClassPropertyNotFoundException(Integer id) {
		super("Object class property not found " + id);
	}
}
