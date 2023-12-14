package com.client.client.controllers;

//move this into client controller
import com.client.client.ClientApplication;
import com.client.client.models.ClientModel;
import com.server.server.models.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import java.util.Optional;

public class loginController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private TextField emailField;

    private ClientModel model;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new ClientModel();
        model.setEmail(emailField.getText());
    }

    /**
     * Checks if the email and username are valid and loads the client window
     */
    public void login() {
        boolean valid = Email.isValidFormat(emailField.getText());
        if (emailField.getText() != null && valid) {
            model.setEmail(emailField.getText());// redundant because at model creation the email is null
            if (model.login()) {
                openClient();
            }
        } else if(!valid){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid email");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Connection error");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Checks if the email and username are valid and loads the client window
     */
    public void register() {
        if (emailField.getText() != null && Email.isValidFormat(emailField.getText())) {
            model.setEmail(emailField.getText());
            if (model.register(emailField.getText())) {
                openClient();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid email");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    private void openClient() {
        Stage stage = (Stage) loginButton.getScene().getWindow();

        loadClient(stage);
        stage.close();

    }

    /**
     * Loads the client window
     */
    private void loadClient(Stage stage) {
        try {
            stage = new Stage();
            URL resource = getClass().getResource("/com/client/client/controllers/client.fxml");
            if (resource == null) {
                throw new FileNotFoundException("Cannot find resource: /com/client/client/controllers/client.fxml");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            ClientController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setOwner(emailField.getText());

            // minimum window size can only be set through the stage
            stage.setMinWidth(400);
            stage.setMinHeight(300);

            // Load the Client icon
            Image icon = new Image(Objects.requireNonNull(ClientApplication.class.getResource("icons/mail.png")).toExternalForm());
            // Set the icon for the stage
            stage.getIcons().add(icon);

            stage.setTitle("Client");
            stage.setScene(scene);
            stage.setOnCloseRequest(controller::handleCloseRequest);

            stage.show();

        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println("Error: " + e + e.getCause());
        }
    }
}
