package com.client.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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