module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.server.server to javafx.fxml;
    exports com.server.server;
}