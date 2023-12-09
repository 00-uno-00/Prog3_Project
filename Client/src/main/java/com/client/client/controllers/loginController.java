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

    /**
     * Checks if the email and username are valid and loads the client window
     */
    public void login() {
        /*if (emailField.getText() != null && Email.isValidFormat(emailField.getText())) {
            
            if (model.login()) {
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("login Error");
                Optional<ButtonType> result = alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid email");
            Optional<ButtonType> result = alert.showAndWait();
        }
        */model.setEmail(emailField.getText());
        openClient(model);
        Stage lstage = (Stage) loginButton.getScene().getWindow();
        lstage.close();
    }

    /**
     * Checks if the email and username are valid and loads the client window
     */
    public void register() {
        if (emailField.getText() != null && Email.isValidFormat(emailField.getText())) {
            model.setEmail(emailField.getText());
            if (model.register(emailField.getText())) {
                openClient(model);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid email");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Loads the client window
     */
    private void loadClient(ClientController controller) {
        try {
            URL resource = getClass().getResource("/com/client/client/controllers/client.fxml");
            if (resource == null) {
                throw new FileNotFoundException("Cannot find resource: /com/client/client/controllers/client.fxml");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            // minimum window size can only be set through the stage
            stage.setMinWidth(400);
            stage.setMinHeight(300);

            stage.setTitle("Client");
            stage.setScene(scene);

            // Intercept the close request
            controller = fxmlLoader.getController();
            controller.setModel(model);  // Set the model here
            stage.setOnCloseRequest(controller::handleCloseRequest);

            stage.show();

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e + e.getCause());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new ClientModel();
        model.setEmail(emailField.getText());
    }

    private void openClient(ClientModel model){
        ClientController controller = new ClientController(model);
        controller.setOwner(emailField.getText());
        loadClient(controller);

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}
