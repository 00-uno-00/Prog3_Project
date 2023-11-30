package com.server.server.controllers;

import com.server.server.ServerApplication;
import com.server.server.models.Logger;
import com.server.server.models.ServerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Label connectionLabel = new Label();

    @FXML
    private ListView<String> logList;

    @FXML
    private ListView<String> dateList; // Assuming you have a ListView for dates

    @FXML
    private ImageView statusIcon;

    private final ObservableList<String> logItems = FXCollections.observableArrayList();
    private final ObservableList<String> dateItems = FXCollections.observableArrayList(); // ObservableList for dates

    private static ServerModel server;

    private final Image onIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/on-button.png")).toExternalForm());
    private final Image offIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/off-button.png")).toExternalForm());

    private int port;

    public void handleCloseRequest(javafx.stage.WindowEvent event) {
        if (server.isOn()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Server is currently running");
            alert.setContentText("Are you sure you want to stop the server?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                server.stopServer();
            } else {
                event.consume();
            }
        } else {
            server.stopServer();
        }
    }

    @FXML
    protected void onOffButtonClick() {
        if (server.isOn()) {
            server.stopServer();
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        } else {
            connectionLabel.setText("CONNECTED to port : " + port);
            statusIcon.setImage(onIcon);
            server.startServer();
        }
        logList.scrollTo(logList.getItems().size() - 1);
        dateList.scrollTo(dateList.getItems().size() - 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (connectionLabel != null) {
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        }
        Logger logger = Logger.getInstance();

        // Request a free port
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
        } catch (IOException e) {
            logger.log("An error occurred while trying to get a free port");
        }

        logList.setItems(logItems);
        dateList.setItems(dateItems); // Set items for dateList

        logger.latestLogEventProperty().addListener((obs, oldLogEvent, newLogEvent) -> {
            if (newLogEvent != null) {
                logItems.add(newLogEvent);
            }
        });

        // Add listener for latestLogDateProperty
        logger.latestLogDateProperty().addListener((obs, oldLogDate, newLogDate) -> {
            if (newLogDate != null) {
                dateItems.add(newLogDate);
            }
        });

        server = new ServerModel();
    }
}