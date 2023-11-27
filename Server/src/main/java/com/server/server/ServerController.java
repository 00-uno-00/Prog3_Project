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

public class ServerController implements Initializable {
    @FXML
    private Label connectionLabel = new Label();

    @FXML
    private ListView<String> logList;

    @FXML
    private ImageView statusIcon;

    private boolean isOn = false;

    private Logger logger;

    private final Image onIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/on-button.png")).toExternalForm());

    private final Image offIcon = new Image(Objects.requireNonNull(ServerApplication.class.getResource("icons/off-button.png")).toExternalForm());


    private final int port = 8025; //assign port number in runtime
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (connectionLabel != null) {
        connectionLabel.setText("NOT RUNNING");
            statusIcon.setImage(offIcon);
        }
        logger = new Logger();
    }
}