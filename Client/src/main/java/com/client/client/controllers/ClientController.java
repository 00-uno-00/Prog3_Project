package com.client.client.controllers;

import com.client.client.Contact;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class ClientController implements Initializable {

    @FXML
    private Button changeAccount;

    @FXML
    private ListView<String> contactsList;

    @FXML
    private TextField recipient;

    private List<Contact> contacts = new ArrayList<>();

    private List<Stage> emailStages = new ArrayList<>();

    Parent root;
    Scene scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeAccount.getStyleClass().setAll("btn","btn-default");

        loadContacts();
        contactsList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            openPopup(contactsList.getSelectionModel().getSelectedItem());
        });

    }

    public void openPopup(String contactName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("email.fxml"));
            root = loader.load();

            emailController emailController = loader.getController();
            emailController.setRecipient(contactName);

            scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("New Email to: " + contactName);
            stage.setScene(scene);
            emailStages.add(stage);
            emailStages.get(emailStages.size()-1).setOnCloseRequest(this::popStage);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error opening popup");
        }
    }

    /**
     * Load contacts from a CSV file
     */
    public void loadContacts() {
        try {
            File contactsFile = new File(getClass().getResource("contacts.csv").getFile());//use getClass().getResource("contacts.csv").getFile() or aneurysm
            Scanner scanner = new Scanner(contactsFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] contactData = line.split(",");
                contacts.add(new Contact(contactData[0], contactData[1]));
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }

        for (Contact contact : contacts) {
            contactsList.getItems().add(contact.getName());
        }
    }

    public void handleCloseRequest(javafx.stage.WindowEvent event) {
        if (emailStages.size() != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Client has open emails");
            alert.setContentText("Are you sure you want to stop the client? Active emails will be closed.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                for (Stage stage : emailStages) {
                    stage.close();
                }
                System.exit(0);
            } else {
                event.consume();
            }
        } else {
            System.exit(0);
        }
    }

    public void popStage(javafx.stage.WindowEvent event){
        emailStages.remove(event.getSource());
    }
}
