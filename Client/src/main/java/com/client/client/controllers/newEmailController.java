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
import javafx.stage.WindowEvent;

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

    /**
     * Displays the new email popup window.
     * @param root The root node of the scene.
     * @param controller The client controller.
     * @return The stage of the new email window.
     * @throws IOException If an I/O error occurs.
     */
    public Stage showNewEmailPopup(Parent root, ClientController controller) throws IOException {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("New Email");
        stage.setScene(scene);
        this.controller = controller;
        stage.show();
        return stage;
    }

    /**
     * Sets the recipient of the new email.
     * @param recipient The email address of the recipient.
     */
    public void setRecipient(String recipient) {
        this.recipient.setText(recipient);
    }

    /**
     * Sets the subject of the new email.
     * @param subject The subject text.
     */
    public void setSubject(String subject) {
        this.subject.setText(subject);
    }

    /**
     * Sets the body content of the new email.
     * @param body The body text of the email.
     */
    public void setBody(String body) {
        this.body.setText(body);
    }

    /**
     * Sets the owner (sender) of the new email.
     * @param owner The email address of the sender.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Closes the new email popup without sending the email.
     */
    public void abort() {
        Stage stage = (Stage) recipient.getScene().getWindow();
        stage.close();
    }

    /**
     * Sends the email and closes the popup if successful.
     */
    public void send() {
        email.setDate(new Date());
        email.setSender(owner);
        email.setRecipients(parseRecipients(recipient.getText()));
        email.setBody(body.getText());
        email.setSubject(subject.getText());
        try {
            if (controller.sendEmail(email)) {
                Stage stage = (Stage) recipient.getScene().getWindow();
                stage.fireEvent(
                        new WindowEvent(
                                stage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Parses the recipients string into a list of email addresses.
     * @param recipients Comma-separated string of email addresses.
     * @return List of email addresses.
     */
    private List<String> parseRecipients(String recipients) {
        List<String> recipientList = new ArrayList<>();
        String[] recipientArray = recipients.split(",");
        for (String recipient : recipientArray) {
            recipientList.add(recipient.trim());
        }
        return recipientList;
    }
}
