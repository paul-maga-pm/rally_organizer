module java.business.main {
    exports com.rallies.business.services.impl;

    requires java.exceptions.main;
    requires java.domain.main;
    requires java.dataaccess.main;
    requires org.apache.logging.log4j;
}