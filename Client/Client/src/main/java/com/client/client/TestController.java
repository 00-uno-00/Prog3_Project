package com.client.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    @FXML
    private Button Reply;

    @FXML
    private Button ReplyAll;

    @FXML
    private Button Forward;

    @FXML
    private Button Delete;


    @FXML
    private Button ChangeAccount;

    /**
     * Set Bootstrap CSS for the client
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reply.getStyleClass().setAll("btn","btn-default");
        ReplyAll.getStyleClass().setAll("btn","btn-default");
        Forward.getStyleClass().setAll("btn","btn-primary");
        Delete.getStyleClass().setAll("btn","btn-danger");
        ChangeAccount.getStyleClass().setAll("btn","btn-default");
    }

}
