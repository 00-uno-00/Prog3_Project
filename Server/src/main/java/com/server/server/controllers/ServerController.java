package com.server.server.controllers;

import com.server.server.ServerApplication;
import com.server.server.models.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static Logger logger;

    // Images for the on and off states of the server
    private final Image onIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/on-button.png")).toExternalForm());
    private final Image offIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/off-button.png")).toExternalForm());

    // Port number for the server
    private final int port = 8025;

    private final URI uri;

    {
        try {
            uri = Objects.requireNonNull(getClass().getResource("id/")).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e+ " URI Syntax Exception");
        }
    }

    private static File idFile;
    /**The ID of the emails being sent
     * Atomic so each Thread can access it and increment it
     */
    private static AtomicInteger id;

    /**
     * Retrieve the old Mail ID from file or creates a new one
     */
    private AtomicInteger retrieveID() {
        idFile = new File(new File(uri), "id.txt");
        try {
            if (!idFile.exists() && idFile.createNewFile()) {
                logger.log("Created Mail ID file (default: 0) " + new Date());
            }
            Scanner scanner = new Scanner(idFile);
            id = new AtomicInteger(scanner.hasNextInt() ? scanner.nextInt() : 0);
            logger.log("Retrieved Mail ID from file (" + id.get() + ") " + new Date());
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException("Error handling id file", e);
        }
        return id;
    }

    /**
     * Stores the current id value into the id.txt file.
     */
    private void storeId() {
        try (PrintWriter writer = new PrintWriter(idFile)) {
            writer.println(id);
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to id file");
        }
    }

    /**
     * Method to handle the close request of the server.
     * If the server is on, it will be turned off by calling the onOffButtonClick method.
     */
    public void handleCloseRequest() {
        if (isOn) {
            onOffButtonClick();
        }
        storeId();
    }

    /**
     * Handles the button click event.
     * Toggles the server state and updates the GUI accordingly.
     */
    @FXML
    protected void onOffButtonClick() {
        isOn = !isOn; // toggle the state
        if (isOn) {
            logList.getItems().add("Started Server "+new Date()); //TODO this does not write on the log file
            connectionLabel.setText("CONNECTED to port : " + port);
            statusIcon.setImage(onIcon);
            id = retrieveID();
        } else {
            logList.getItems().add("Stopped Server "+new Date());
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
            storeId();
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