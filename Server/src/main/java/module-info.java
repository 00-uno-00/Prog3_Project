module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires junit;

    opens com.server.server to javafx.fxml;
    exports com.server.server;
    exports com.server.server.models;
    opens com.server.server.models to javafx.fxml, com.google.gson;
    exports com.server.server.controllers;
    opens com.server.server.controllers to javafx.fxml;
    exports com.server.server.tests;
    opens com.server.server.tests to javafx.fxml;
}