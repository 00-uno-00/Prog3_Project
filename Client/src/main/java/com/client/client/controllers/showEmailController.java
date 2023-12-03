package com.client.client.controllers;

import com.client.client.models.Email;
import com.client.client.controllers.newEmailController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class showEmailController {

    @FXML
    private Button replyButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField sender;

    @FXML
    private TextField subject;

    @FXML
    private TextArea body;

    Parent root;

    public void showEmailPopup(Email email) {
        sender.setText(email.getSender());
        subject.setText(email.getSubject());
        body.setText(email.getBody());
    }

    public void reply() {
        newEmailController controller = new newEmailController();
        controller.setRecipient(sender.getText());
        controller.setSubject("Re: " + subject.getText());
        controller.setBody("\"" + body.getText() + "\"");
        try {
            controller.showNewEmailPopup(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forward() {

    }

    public void delete() {

    }

    public Stage showEmailPopup(Email email, Parent root) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("showEmail.fxml"));
        root = loader.load();

        showEmailController showEmailController = loader.getController();
        showEmailController.showEmailPopup(email);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Email");
        stage.setScene(scene);
        stage.show();
        return stage;

    }
}
