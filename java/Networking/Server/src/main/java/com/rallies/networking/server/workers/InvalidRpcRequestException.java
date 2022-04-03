package com.rallies.networking.server.workers;

import com.rallies.exceptions.ExceptionBaseClass;

public class InvalidRpcRequestException extends ExceptionBaseClass {
    public InvalidRpcRequestException() {
    }

    public InvalidRpcRequestException(String message) {
        super(message);
    }

    public InvalidRpcRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRpcRequestException(Throwable cause) {
        super(cause);
    }

    public InvalidRpcRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
