module com.client.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.example.server;

    opens com.client.client to javafx.fxml;
    exports com.client.client;
    exports com.client.client.controllers;
    opens com.client.client.controllers to javafx.fxml;
    exports com.client.client.models;
    opens com.client.client.models to javafx.fxml;
}