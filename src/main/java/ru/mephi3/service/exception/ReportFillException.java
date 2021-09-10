package ru.mephi3.service.exception;

public class ReportFillException extends Exception{
    public ReportFillException() {
    }

    public ReportFillException(String message) {
        super(message);
    }

    public ReportFillException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportFillException(Throwable cause) {
        super(cause);
    }

    public ReportFillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
