package validators.interfaces;

import errors.ExceptionBaseClass;
import models.Identifiable;

public interface Validator <ID, M extends Identifiable<ID>> {
    void validate(M model);

    class InvalidModelException extends ExceptionBaseClass {
        public InvalidModelException() {
        }

        public InvalidModelException(String message) {
            super(message);
        }

        public InvalidModelException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidModelException(Throwable cause) {
            super(cause);
        }

        public InvalidModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
