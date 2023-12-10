package com.client.client.models;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ClientModel {

    private ExecutorService executorService;

    private CommsHandler commsHandler;

    private String email;

    public ClientModel() {
        try {
            executorService = Executors.newFixedThreadPool(2);
            commsHandler = new CommsHandler(executorService, email);
            System.out.println("Client model started");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login() {
        try {
            String response = commsHandler.login();
            if ("successful" == response) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Register Error");
                alert.setContentText(response);
                Optional<ButtonType> result = alert.showAndWait();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(String email) {
        try {
            String response = commsHandler.register();
            if ("successful" == response) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Register Error");
                alert.setContentText(response);
                Optional<ButtonType> result = alert.showAndWait();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Email> refresh(List<Integer> emailIDs) {
        try {
            return commsHandler.refresh(emailIDs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean send(Email email) {
        try {
            String response = commsHandler.send(email);
            if (response == "successful") {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Send Error");
                alert.setContentText(response);
                Optional<ButtonType> result = alert.showAndWait();
                return false;
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setEmail(String email) {
        this.email = email;
        commsHandler.setEmail(email);
    }
}
