package ru.mephi3.service.exception;

public class ObjectClassNotFoundException extends RuntimeException {

    public ObjectClassNotFoundException(Integer id) {
        super("Object class not found " + id);
    }
}
