package ru.mephi3.service.exception;

public class DocumentTypeNotFoundException extends RuntimeException {

	public DocumentTypeNotFoundException(Integer id) {
		super("Вид документа не найден с id " + id);
	}
}
