package com.client.client.models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.server.server.models.Email;

/**
* ClientModel handles client-side functionalities including communication with the server.
* It manages tasks like login, registration, email operations, and communication setup.
*/
public class ClientModel {

    private ExecutorService executorService;

    private CommsHandler commsHandler;

    private String email;


    /**
     * Constructor to initialize the ClientModel with necessary components.
     * It sets up the executor service and communication handler.
     */
    public ClientModel() {
        try {
            executorService = Executors.newFixedThreadPool(2);
            commsHandler = new CommsHandler(executorService, email);
            System.out.println("Client model started");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to log in the user.
     * @return "successful" if login is successful, an error message otherwise. Shows an error alert in case of failure.
     */
    public String login() {
        try {
            String response = commsHandler.login();
            if ("successful".equals(response)) {
                return "successful";
            } else {
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to register a new user with the given email.
     * @param email The email address for the new user.
     * @return "successful" if registration is successful, an error message otherwise. Throws a RuntimeException in case of failure.
     */
    public String register(String email) {
        try {
            String response = commsHandler.register();
            if ("successful".equals(response)) {
                return "successful";
            } else {
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes and retrieves a list of emails based on provided IDs.
     * @param emailIDs A list of email IDs to be refreshed.
     * @return A list of Email objects corresponding to the email IDs.
     */
    public List<Email> refresh(List<Integer> emailIDs) {
        try {
            return commsHandler.refresh(emailIDs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an email.
     * @param email The Email object to be sent.
     * @return "successful" if the email is sent successfully, an error message otherwise. Throws a RuntimeException in case of failure.
     */
    public String send(Email email) {
        try {
            String response = commsHandler.send(email);
            if (Objects.equals(response, "successful")) {
                return "successful";
            } else {
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes an email with the specified ID.
     * @param ID The ID of the email to be deleted.
     * @return "successful" if the email is deleted successfully, an error message otherwise. Throws a RuntimeException in case of failure.
     */
    public String delete(Integer ID) {
        try {
            String response = commsHandler.delete(ID);
            if (Objects.equals(response, "successful")) {
                return "successful";
            } else {
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Marks an email as read with the specified ID.
     * @param ID The ID of the email to be marked as read.
     * @return "successful" if the operation is successful, an error message otherwise. Throws a RuntimeException in case of failure.
     */
    public String read(Integer ID) {
        try {
            String response = commsHandler.read(ID);
            if (Objects.equals(response, "successful")) {
                return "successful";
            } else {
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the email address of the client.
     * @param email The email address to be set for the client.
     */
    public void setEmail(String email) {
        this.email = email;
        commsHandler.setSender(email);
    }

}
