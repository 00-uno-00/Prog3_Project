package com.client.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import com.fasterxml.jackson.databind.ObjectMapper;
public class emailController implements Initializable {

    @FXML
    public Button sendButton;

    @FXML
    public Button abortButton;

    @FXML
    private TextField recipient;

    @FXML
    private TextField subject;

    @FXML
    private TextArea body;

    private Contact owner = new Contact("zioper","help"); // must be initialized with a valid contact

    private Email email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email = new Email(owner.getEmail(),recipient.getText(),body.getText(),subject.getText(), null, false );//date and time will be updated ta the moment the email is sent
    }

    public void setRecipient(String recipient) {
        this.recipient.setText(recipient);
    }

    public void setOwner(Contact owner) {
        this.owner = owner;
    }

    public void abort() {
        Stage stage = (Stage) recipient.getScene().getWindow();
        stage.close();
    }

    public void send() {
        email.setDate(new Date());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(email);
            System.out.println(json);
        } catch (Exception e) {
            System.out.println("Error converting email to json");
            e.printStackTrace();
        }
        //TODO: send email
    }
}
