package com.client.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientController implements Initializable {

    @FXML
    private Button changeAccount;

    @FXML
    private ListView<String> contactsList;

    @FXML
    private TextField recipient;

    private List<Contact> contacts = new ArrayList<>();

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

        //TODO: add listener so that when the main client application is cloised, the email application is also closed
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
}
