package com.server.server.controllers;

import com.server.server.ServerApplication;
import com.server.server.models.Logger;
import com.server.server.models.ServerModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for the server.
 * This class handles the UI interactions for the server.
 */

public class ServerController implements Initializable {

    @FXML
    private Label connectionLabel = new Label();
    @FXML
    private ImageView statusIcon;
    @FXML
    private ListView<String> logList;
    @FXML
    private ListView<String> dateList; // ListView for dates
    @FXML
    private ListView<String> typeList; // ListView for log types

    private final ObservableList<String> logItems = FXCollections.observableArrayList();
    private final ObservableList<String> dateItems = FXCollections.observableArrayList(); // ObservableList for dates
    private final ObservableList<String> typeItems = FXCollections.observableArrayList(); // ObservableList for log types
    private static ServerModel server;

    private final Image onIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/on-button.png")).toExternalForm());
    private final Image offIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/off-button.png")).toExternalForm());

    /**
     * Method to get the vertical scrollbar of a ListView.
     * @param listView ListView to get the scrollbar from
     * @return vertical scrollbar of the ListView
     */
    private ScrollBar getVerticalScrollbar(ListView<?> listView) {
        ScrollBar scrollbar = null;
        for (Node node : listView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar bar) {
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    scrollbar = bar;
                }
            }
        }
        return scrollbar;
    }

    /**
     * Method to bind two scrollbars together.
     * @param sb1 first scrollbar
     * @param sb2 second scrollbar
     */
    private void bindScrollBars(ScrollBar sb1, ScrollBar sb2) {
        sb1.valueProperty().bindBidirectional(sb2.valueProperty());
    }

    /**
     * Method called to initialize the controller after its root element has been completely processed.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (connectionLabel != null) {
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        }
        Logger logger = Logger.getInstance();

        logList.setItems(logItems); // Set items for logList
        dateList.setItems(dateItems); // Set items for dateList
        typeList.setItems(typeItems); // Set items for typeList

        logger.latestLogEventProperty().addListener((obs) -> {
            String newLogEvent = logger.latestLogEventProperty().get();
            if (newLogEvent != null) {
                logItems.add(newLogEvent);
            }
        });

        // Add InvalidationListener for latestLogDateProperty
        logger.latestLogDateProperty().addListener((obs) -> {
            String newLogDate = logger.latestLogDateProperty().get();
            if (newLogDate != null) {
                dateItems.add(newLogDate);
            }
        });

        // Add InvalidationListener for latestLogTypeProperty
        logger.latestLogTypeProperty().addListener((obs) -> {
            String newLogType = logger.latestLogTypeProperty().get();
            if (newLogType != null) {
                typeItems.add(newLogType);
            }
        });

        //async binding of scrollbars to avoid blocking the UI thread
        Platform.runLater(() -> {
            ScrollBar logListScrollBar = getVerticalScrollbar(logList);
            ScrollBar dateListScrollBar = getVerticalScrollbar(dateList);
            ScrollBar typeListScrollBar = getVerticalScrollbar(typeList);

            bindScrollBars(logListScrollBar, typeListScrollBar);
            bindScrollBars(typeListScrollBar, dateListScrollBar);
            bindScrollBars(dateListScrollBar, logListScrollBar);
        });

        server = new ServerModel();
    }

    /**
     * Method to handle the close request event.
     * @param event WindowEvent
     */
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

    /**
     * Method to handle the clearLog button click event.
     */
    @FXML
    protected void clearLogButtonClick() {
        logItems.clear();
        dateItems.clear();
        typeItems.clear();
    }

    /**
     * Method to handle the on/off button click event.
     */
    @FXML
    protected void onOffButtonClick() throws InterruptedException {
        if (server.isOn()) {
            server.stopServer();
            connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        } else {
            statusIcon.setImage(onIcon);
            server.startServer();
            connectionLabel.setText("CONNECTED to port : " + server.getPort());
        }
        Thread.sleep(100); //avoid button smashing
        Platform.runLater(() -> {
            logList.scrollTo(logList.getItems().size() - 1);
            dateList.scrollTo(dateList.getItems().size() - 1);
            typeList.scrollTo(typeList.getItems().size() - 1);
        });
    }
}