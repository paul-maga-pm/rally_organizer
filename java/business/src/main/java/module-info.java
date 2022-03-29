module java.business.main {
    exports business.exceptions;
    exports business.services;

    requires java.exceptions.main;
    requires java.domain.main;
    requires java.dataaccess.main;
    requires org.apache.logging.log4j;
}