package ru.mephi3.service.exception;

public class ReportCompileException extends Exception{
    public ReportCompileException() {
    }

    public ReportCompileException(String message) {
        super(message);
    }

    public ReportCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportCompileException(Throwable cause) {
        super(cause);
    }

    public ReportCompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
