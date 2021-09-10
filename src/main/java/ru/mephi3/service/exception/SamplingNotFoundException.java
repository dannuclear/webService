package ru.mephi3.service.exception;

public class SamplingNotFoundException extends RuntimeException {

	public SamplingNotFoundException(Integer id) {
		super("Sampling not found " + id);
	}
}
