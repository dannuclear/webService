package ru.mephi3.service.exception;

public class SampleNotFoundException extends RuntimeException {

	public SampleNotFoundException(Integer id) {
		super("Sample not found " + id);
	}
}
