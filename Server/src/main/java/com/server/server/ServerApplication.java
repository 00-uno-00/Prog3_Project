package com.server.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load icon
        Image icon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/server.png")).toExternalForm());
        // Set the icon for the stage
        stage.getIcons().add(icon);
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.setMinWidth(120);
        stage.setMinHeight(120);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}