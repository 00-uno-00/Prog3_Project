package com.client.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class GPT4Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Toolbar with buttons
        ToolBar toolBar = new ToolBar(
                new Button("Reply"),
                new Button("Reply All"),
                new Button("Forward"),
                new Button("Delete")
        );

        // Email list on the left side
        ListView<String> emailList = new ListView<>();
        emailList.getItems().addAll("Email 1", "Email 2", "Email 3"); // Add your emails here

        // Email content in the center
        TextArea emailContent = new TextArea();

        // User details on the right side
        VBox userDetails = new VBox(
                new Button("Change Account"),
                new Label("Contact 1"),
                new Label("Contact 2"),
                new Label("Contact 3")
                // Add more user details here
        );

        // Layout setup
        root.setTop(toolBar);
        root.setLeft(emailList);
        root.setCenter(emailContent);
        root.setRight(userDetails);

        // Applying BootstrapFX styling
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setTitle("Email Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}