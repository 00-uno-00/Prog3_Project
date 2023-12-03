package com.client.client.controllers;

import com.client.client.models.Contact;
import com.client.client.models.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class newEmailController implements Initializable {

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

    private Contact owner = new Contact(""); // must be initialized with a valid contact

    private Email email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email = new Email(owner.getEmail(), parseRecipients(recipient.getText()), body.getText(), subject.getText(), null, false);// date
                                                                                                                 // and
                                                                                                                 // time
                                                                                                                 // will
                                                                                                                 // be
                                                                                                                 // updated
                                                                                                                 // at
                                                                                                                 // the
                                                                                                                 // moment
                                                                                                                 // the
                                                                                                                 // email
                                                                                                                 // is
                                                                                                                 // sent
    }

    public Stage showNewEmailPopup(Parent root) throws IOException {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("New Email");
        stage.setScene(scene);
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

    public void setOwner(Contact owner) {
        this.owner = owner;
    }

    public void abort() {
        Stage stage = (Stage) recipient.getScene().getWindow();
        stage.close();
    }

    public void send() {
        email.setDate(new Date());

        // TODO: send email
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
