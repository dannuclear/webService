package ru.mephi3.service.exception;

public class ReportIOException extends Exception{
    public ReportIOException() {
    }

    public ReportIOException(String message) {
        super(message);
    }

    public ReportIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportIOException(Throwable cause) {
        super(cause);
    }

    public ReportIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
