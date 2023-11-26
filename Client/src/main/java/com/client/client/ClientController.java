package com.client.client;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private VBox window;

    @FXML
    private ListView<String> contactsList;

    private List<Contact> contacts = new ArrayList<>();

    private Stage stage = new Stage();
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeAccount.getStyleClass().setAll("btn","btn-default");

        loadContacts();
    }

    public void openPopup(MouseEvent event) {
        try {
            root = javafx.fxml.FXMLLoader.load(getClass().getResource("email.fxml"));
            scene = new Scene(root);
            stage.setTitle("New Email");
            stage.setScene(scene);
            stage.show();


            // disable main window while popup is open
            window.setDisable(true);
            // TODO: add listener to enable main window when popup is closed
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
                contacts.add(new Contact(contactData[0], contactData[1], contactData[2]));
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }

        for (Contact contact : contacts) {
            contactsList.getItems().add(contact.getName() + " " + contact.getSurname());
        }
    }
}
