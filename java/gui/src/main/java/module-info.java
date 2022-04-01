module com.rallies.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires java.dataaccess.main;
    requires java.business.main;
    requires java.domain.main;
    requires java.exceptions.main;

    exports com.rallies.gui.impl.controllers to javafx.fxml;
    opens com.rallies.gui.impl.controllers to javafx.fxml;
    opens com.rallies.gui.impl to javafx.graphics;
}