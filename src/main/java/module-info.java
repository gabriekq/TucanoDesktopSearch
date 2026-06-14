module com.mendonca.tucanodesktopsearch {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.mendonca.tucanodesktopsearch to javafx.fxml;
    exports com.mendonca.tucanodesktopsearch;
    exports com.mendonca.gui;
    opens com.mendonca.gui to javafx.fxml;
    exports com.mendonca.search;
    opens com.mendonca.search to javafx.fxml;
}