package com.server.server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * ServerController class that controls the server's GUI.
 * It implements Initializable for initializing the controller after its root element has been completely processed.
 */
public class ServerController implements Initializable {
    @FXML
    private Label connectionLabel = new Label();

    @FXML
    private ListView<String> logList;

    @FXML
    private ImageView statusIcon;

    private boolean isOn = false;

    private Logger logger;

    // Images for the on and off states of the server
    private final Image onIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/on-button.png")).toExternalForm());
    private final Image offIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/off-button.png")).toExternalForm());

    // Port number for the server
    private final int port = 8025;

    /**
     * Method to handle the close request of the server.
     * If the server is on, it will be turned off by calling the onOffButtonClick method.
     */
    public void handleCloseRequest() {
        if (isOn) {
            onOffButtonClick();
        }
    }
    /**
     * Handles the button click event.
     * Toggles the server state and updates the GUI accordingly.
     */
    @FXML
    protected void onOffButtonClick() {
        isOn = !isOn; // toggle the state
        if (isOn) {
            logList.getItems().add("Started Server "+new Date());
            connectionLabel.setText("CONNECTED to port : " + port);
            statusIcon.setImage(onIcon);
        } else {
            logList.getItems().add("Stopped Server "+new Date());
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        }
        //log to csv file
        logger.log(logList.getItems().get(logList.getItems().size()-1));
        // Scroll to the last item
        logList.scrollTo(logList.getItems().size() - 1);
    }

    /**
     * Initializes the controller class.
     * This method is automatically called after the fxml file has been loaded.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (connectionLabel != null) {
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        }
        logger = new Logger();
    }
}