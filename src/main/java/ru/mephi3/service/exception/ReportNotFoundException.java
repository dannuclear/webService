package ru.mephi3.service.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(Integer id) {
        super("Report not found " + id);
    }
}
