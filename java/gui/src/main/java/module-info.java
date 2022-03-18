module com.rallies.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.rallies.gui to javafx.graphics;
    requires java.dataaccess.main;
    requires java.business.main;
    requires java.domain.main;
    requires java.exceptions.main;

    exports com.rallies.gui.controllers to javafx.fxml;
    opens com.rallies.gui.controllers to javafx.fxml;
}