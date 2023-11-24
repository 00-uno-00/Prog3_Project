package com.client.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GPT3Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        ListView<String> emailList = new ListView<>();
        emailList.getItems().addAll("Email 1", "Email 2", "Email 3");

        TextArea emailContent = new TextArea("Here is the content of the selected email...");

        Button replyButton = new Button("Reply");
        Button replyAllButton = new Button("Reply All");
        Button forwardButton = new Button("Forward");
        Button deleteButton = new Button("Delete");

        // ToolBar for email actions
        ToolBar emailActionsToolBar = new ToolBar(replyButton, replyAllButton, forwardButton, deleteButton);

        // Set up the layout
        BorderPane root = new BorderPane();

        VBox emailListColumn = new VBox(emailList);
        VBox emailContentColumn = new VBox(emailActionsToolBar, emailContent);
        Button changeAccountButton = new Button("Change Account");

        ListView<String> userDetails = new ListView<>();
        userDetails.getItems().addAll("Contact 1", "Contact 2", "Contact 3");

        VBox userInfoColumn = new VBox(changeAccountButton, userDetails);

        root.setLeft(emailListColumn);
        root.setCenter(emailContentColumn);
        root.setRight(userInfoColumn);

        // Create the scene
        Scene scene = new Scene(root, 800, 600);

        // Set the stage properties
        primaryStage.setTitle("Email Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
