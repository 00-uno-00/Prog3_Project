package com.client.client.controllers;

import com.client.client.models.Contact;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import com.client.client.models.Services;

import java.util.Optional;

public class loginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    private Stage stage = new Stage();

    public Services services = new Services();

    /**
     * Checks if the email and username are valid and loads the client window
     */
    public void login() {
        System.out.println(Services.isValidEmail(emailField.getText()));
        if (usernameField.getText() != null && emailField.getText() != null
                && Services.isValidEmail(emailField.getText())) {
            Contact owner = new Contact(usernameField.getText(), emailField.getText());

            ClientController controller = new ClientController();
            controller.setOwner(owner);
            loadClient(controller);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid email or username");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Loads the client window
     */
    private void loadClient(ClientController controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            // minimum window size can only be set through the stage
            stage.setMinWidth(400);
            stage.setMinHeight(300);

            stage.setTitle("Client");
            stage.setScene(scene);

            // Intercept the close request
            controller = fxmlLoader.getController();
            stage.setOnCloseRequest(controller::handleCloseRequest);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
