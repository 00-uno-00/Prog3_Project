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
    public TextField recipients;

    @FXML
    private Label emailDate;

    @FXML
    private Button replyButton;

    @FXML
    public Button replyAllButton;

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

    /**
     * Sets the email details in the popup window.
     * @param email The Email object containing sender, subject, body, and date.
     */
    protected void setEmailPopup(Email email) {
        sender.setText(email.getSender());
        recipients.setText(email.getRecipients().toString().replaceAll("[\\[\\]]", ""));
        subject.setText(email.getSubject());
        body.setText(email.getBody());
        emailDate.setText(email.getDate().toString());
    }

    /**
     * Sets the client controller for this view.
     * @param clientController The controller managing client operations.
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Sets the email object to be displayed or manipulated.
     * @param email The Email object to be set.
     */

    public void setEmail(Email email) {
        this.email = email;
    }

    /**
     * Sets the stage for the email view.
     * @param stage The stage on which the email view is displayed.
     */

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initiates the reply operation for the current email.
     */

    public void reply() {
        clientController.replyEmail(email);
    }

    /**
     * Initiates the forward operation for the current email.
     */

    public void forward() {
        clientController.forwardEmail(email);
    }

    /**
     * Initiates the deletion of the current email.
     */

    public void delete() {
        clientController.deleteEmail(email, stage);
    }

    /**
     * Displays the email popup window with the specified email and client controller.
     * @param inputEmail The email to be displayed.
     * @param clientController The controller managing client operations.
     * @return The stage of the displayed email popup.
     * @throws IOException If the FXML file cannot be loaded.
     */

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
