package com.client.client.controllers;

//move this into client controller
import com.client.client.models.ClientModel;
import com.client.client.models.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

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

    private Stage stage = new Stage();

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
        if (emailField.getText() != null && Email.isValidFormat(emailField.getText())) {
            model.setEmail(emailField.getText());// redundant because at model creation the email is null
            if (model.login()) {
                openClient();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid email");
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
        loadClient();

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Loads the client window
     */
    private void loadClient() {
        try {
            URL resource = getClass().getResource("/com/client/client/controllers/client.fxml");
            if (resource == null) {
                throw new FileNotFoundException("Cannot find resource: /com/client/client/controllers/client.fxml");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            ClientController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setOwner(emailField.getText());
            controller.refresh();




            // minimum window size can only be set through the stage
            stage.setMinWidth(400);
            stage.setMinHeight(300);

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
