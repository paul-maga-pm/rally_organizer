package interfaces;

import errors.ExceptionBaseClass;
import models.Identifiable;

public interface Repository <ID, M extends Identifiable<ID>> {
    M save(M model);
    M findOne(ID modelID);
    Iterable<M> findAll();

    class NotImplementedRepositoryMethodException extends ExceptionBaseClass {
        public NotImplementedRepositoryMethodException() {
        }

        public NotImplementedRepositoryMethodException(String message) {
            super(message);
        }

        public NotImplementedRepositoryMethodException(String message, Throwable cause) {
            super(message, cause);
        }

        public NotImplementedRepositoryMethodException(Throwable cause) {
            super(cause);
        }

        public NotImplementedRepositoryMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
