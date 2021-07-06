package ru.mephi3.service.exception;

public class DocumentNotFoundException extends RuntimeException {

	public DocumentNotFoundException(Integer id) {
		super("Документ с id " + id);
	}
}
