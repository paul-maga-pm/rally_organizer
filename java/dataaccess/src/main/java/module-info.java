module java.dataaccess.main {
    requires java.domain.main;
    requires java.exceptions.main;
    requires org.apache.logging.log4j;
    requires java.sql;

    exports com.rallies.dataaccess.repository.impl.database;
    exports com.rallies.dataaccess.repository.api;
}