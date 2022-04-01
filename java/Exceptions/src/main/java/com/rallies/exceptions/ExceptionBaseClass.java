package com.rallies.exceptions;

public class ExceptionBaseClass extends RuntimeException{
    public ExceptionBaseClass() {
    }

    public ExceptionBaseClass(String message) {
        super(message);
    }

    public ExceptionBaseClass(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionBaseClass(Throwable cause) {
        super(cause);
    }

    public ExceptionBaseClass(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
