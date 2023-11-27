package com.server.server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Label connectionLabel = new Label();

    @FXML
    private ListView<String> logList;

    private boolean isOn = false;

    @FXML
    protected void onOffButtonClick() {
        isOn = !isOn; // toggle the state
        if (isOn) {
            connectionLabel.setText("Connection: on");
        } else {
            connectionLabel.setText("Connection: off");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (connectionLabel != null) {
            connectionLabel.setText("Connection: off");
        }
    }
}