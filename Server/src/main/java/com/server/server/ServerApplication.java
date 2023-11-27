package com.server.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

/**
 * Main class for the server application.
 * Extends the JavaFX Application class and overrides the start method.
 */
public class ServerApplication extends Application {
    /**
     * Sets up the stage and scene for the application.
     * @param stage the primary stage for this application, onto which the application scene can be set.
     * @throws IOException if the fxml file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the server icon
        Image icon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/server.png")).toExternalForm());
        // Set the icon for the stage
        stage.getIcons().add(icon);
        // Load the FXML file for the server view
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        // Set the title for the stage
        stage.setTitle("Server");
        // Set the scene for the stage
        stage.setScene(scene);
        // Set the minimum width and height for the stage
        stage.setMinWidth(120);
        stage.setMinHeight(120);
        // Show the stage
        stage.show();
    }

    /**
     * Calls the launch method inherited from the Application class to start the JavaFX application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}