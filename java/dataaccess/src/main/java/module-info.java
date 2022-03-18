module java.dataaccess.main {
    requires java.domain.main;
    requires java.exceptions.main;
    requires org.apache.logging.log4j;
    requires java.sql;

    exports repository.database;
    exports repository.exceptions;
    exports repository.interfaces;
}