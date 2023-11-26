package com.client.client;

import javafx.application.Application;

import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

import javafx.stage.Stage;


public class BLACKBOXTest extends Application {


    @Override

    public void start(Stage primaryStage) {

        // Creating UI elements

        VBox root = new VBox();

        root.setPadding(new Insets(10));

        root.setSpacing(5);


        TextField recipientField = new TextField();

        recipientField.setPromptText("Destinatario");


        TextArea emailTextArea = new TextArea();

        emailTextArea.setPromptText("Scrivi la tua email...");

        emailTextArea.setPrefSize(300, 200);


        Button sendButton = new Button("Invia");

        Button cancelButton = new Button("Annulla");


        // Adding UI elements to the root container

        root.getChildren().addAll(recipientField, emailTextArea, sendButton, cancelButton);


        // Creating a scene

        Scene scene = new Scene(root, 400, 300);


        // Setting the scene on the stage

        primaryStage.setScene(scene);

        primaryStage.setTitle("Email App");

        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);

    }

}