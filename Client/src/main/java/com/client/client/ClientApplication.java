package com.client.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Load the Client icon
        Image icon = new Image(Objects.requireNonNull(ClientApplication.class.getResource("icons/mail.png")).toExternalForm());
        // Set the icon for the stage
        stage.getIcons().add(icon);

        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Login");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}