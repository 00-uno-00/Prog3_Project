package com.client.client.controllers;

import com.client.client.models.ClientModel;
import com.server.server.models.Email;
import com.client.client.controllers.newEmailController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class showEmailController {

    @FXML
    private Label emailDate;
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

    private ClientController clientController;

    private Email email;

    private Stage stage;

    protected void setEmailPopup(Email email) {
        sender.setText(email.getSender());
        subject.setText(email.getSubject());
        body.setText(email.getBody());
        emailDate.setText(email.getDate().toString());
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void reply() {
        clientController.replyEmail(email);
    }

    public void forward() {
        clientController.forwardEmail(email);
    }

    public void delete() {
        clientController.deleteEmail(email, stage);
    }

    public Stage showEmailPopup(Email inputEmail, ClientController clientController) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("showEmail.fxml"));
        Scene scene = new Scene(loader.load());

        showEmailController showEmailController = loader.getController();
        showEmailController.setEmailPopup(inputEmail);
        showEmailController.setClientController(clientController);
        showEmailController.setEmail(inputEmail);

        Stage stage = new Stage();
        stage.setTitle("Email");
        stage.setScene(scene);
        stage.show();

        showEmailController.setStage(stage);

        return stage;
    }
}
