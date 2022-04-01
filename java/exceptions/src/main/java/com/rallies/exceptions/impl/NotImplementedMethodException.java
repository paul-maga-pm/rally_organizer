package com.rallies.exceptions.impl;

public class NotImplementedMethodException extends ExceptionBaseClass {
    public NotImplementedMethodException() {
    }

    public NotImplementedMethodException(String message) {
        super(message);
    }

    public NotImplementedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedMethodException(Throwable cause) {
        super(cause);
    }

    public NotImplementedMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
