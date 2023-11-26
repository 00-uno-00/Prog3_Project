package com.client.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.kordamp.bootstrapfx.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class TestController implements Initializable {

    @FXML
    private Button changeAccount;

    @FXML
    private AnchorPane buttonAnchor;

    @FXML
    private ListView<String> contactsList;

    private List<Contact> contacts = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeAccount.getStyleClass().setAll("btn","btn-default");

        loadContacts();
    }


    /**
     * Load contacts from a CSV file
     */
    public void loadContacts() {
        try {
            File contactsFile = new File("Client\\Client\\src\\main\\resources\\com\\client\\client\\contacts.csv");
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
