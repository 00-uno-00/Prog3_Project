package com.client.client.controllers;

import com.client.client.models.ClientModel;
import com.server.server.models.Email;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class newEmailController {

    @FXML
    public Button sendButton;

    @FXML
    public Button abortButton;

    @FXML
    private TextField recipient;

    @FXML
    private TextField subject;

    @FXML
    private TextArea body;

    ClientController controller;

    private String owner = ""; // must be initialized with a valid contact

    private Email email = new Email();

    public Stage showNewEmailPopup(Parent root, ClientController controller) throws IOException {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("New Email");
        stage.setScene(scene);
        this.controller = controller;
        stage.show();
        return stage;
    }

    public void setRecipient(String recipient) {
        this.recipient.setText(recipient);
    }

    public void setSubject(String subject) {
        this.subject.setText(subject);
    }

    public void setBody(String body) {
        this.body.setText(body);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void abort() {
        Stage stage = (Stage) recipient.getScene().getWindow();
        stage.close();
    }

    public void send() {
        email.setDate(new Date());
        email.setSender(owner);
        email.setRecipients(parseRecipients(recipient.getText()));
        email.setBody(body.getText());
        email.setSubject(subject.getText());
        try {
            if (controller.sendEmail(email)) {
                Stage stage = (Stage) recipient.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> parseRecipients(String recipients) {
        List<String> recipientList = new ArrayList<>();
        String[] recipientArray = recipients.split(",");
        for (String recipient : recipientArray) {
            recipientList.add(recipient.trim());
        }
        return recipientList;
    }
}
