package com.client.client.controllers;

import com.client.client.models.ClientModel;
import com.client.client.models.Contact;
import com.client.client.models.Email;
import com.client.client.models.EmailItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class ClientController implements Initializable {

    @FXML
    private Button changeAccount;

    @FXML
    private Button refresh;

    @FXML
    private ListView<String> contactsList;

    @FXML
    private ListView<EmailItem> senderList;

    @FXML
    private ListView<EmailItem> subjectList;

    @FXML
    private ListView<EmailItem> bodyList;

    private List<Contact> contacts = new ArrayList<>();

    private HashMap<Integer, Email> emails = new HashMap<>();

    private List<Stage> emailStages = new ArrayList<>();

    Parent root;
    Scene scene;
    Stage stage;

    private ClientModel model;

    public ClientController(ClientModel model) {
        this.model = model;
    }

    private Contact owner = new Contact(""); // must be initalized at startup

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeAccount.getStyleClass().setAll("btn", "btn-default");

        loadContacts();

        contactsList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            openNewEmailPopup(new String[]{"new", "1", contactsList.getSelectionModel().getSelectedItem(), "", ""});
        });

        senderList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            EmailItem selectedSender = senderList.getSelectionModel().getSelectedItem();
            Email selectedEmail = emails.get(selectedSender.getId());
            if (selectedEmail != null) {
                openShowEmailPopup(selectedEmail, root);
            }
        });

        subjectList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            EmailItem selectedSubject = subjectList.getSelectionModel().getSelectedItem();
            Email selectedEmail = emails.get(selectedSubject.getId());
            if (selectedEmail != null) {
                openShowEmailPopup(selectedEmail, root);
            }
        });

        bodyList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            EmailItem selectedBody = bodyList.getSelectionModel().getSelectedItem();
            Email selectedEmail = emails.get(selectedBody.getId());
            if (selectedEmail != null) {
                openShowEmailPopup(selectedEmail, root);
            }
        });


        Platform.runLater(() -> {
            ScrollBar senderScrollBar = getVerticalScrollbar(senderList);
            ScrollBar subjectScrollBar = getVerticalScrollbar(subjectList);
            ScrollBar bodyScrollBar = getVerticalScrollbar(bodyList);

            bindScrollBars(senderScrollBar, bodyScrollBar);
            bindScrollBars(bodyScrollBar, subjectScrollBar);
            bindScrollBars(subjectScrollBar, senderScrollBar);
        });
        refresh();
    }

    public void changeAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            root = loader.load();

            scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error opening login popup");
        }
    }

    public void refresh() {
        if (model.refresh(new ArrayList<>(emails.keySet())) == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Connection error");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            for (Email email : model.refresh(new ArrayList<>(emails.keySet()))) {
                handleEmail(email);
            }
        }
    }

    //can be changed into ViewClass
    public void openNewEmailPopup(String[] args) {//args[0] is the type of new mail, args[1] is the number of recipients, args[2] is the recipient(s), args[3] is the subject, args[4] is the body
        switch (args[0]){
            case "new"://args[1] = 1
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    newEmailController.setRecipient(args[2]);
                    newEmailController.setSubject(args[3]);
                    newEmailController.setBody(args[4]);

                    emailStages.add(newEmailController.showNewEmailPopup(root));
                    emailStages.get(emailStages.size() - 1).setOnCloseRequest(this::popStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
            case "reply"://args[1] = 1
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    newEmailController.setRecipient(args[2]);
                    newEmailController.setSubject("Re: " + args[3]);
                    newEmailController.setBody("\"" + args[4] + "\"");

                    emailStages.add(newEmailController.showNewEmailPopup(root));
                    emailStages.get(emailStages.size() - 1).setOnCloseRequest(this::popStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
            case "forward"://args[1] = 1
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    newEmailController.setRecipient(args[2]);
                    newEmailController.setSubject("Fwd: " + args[3]);
                    newEmailController.setBody("\"" + args[4] + "\"");

                    emailStages.add(newEmailController.showNewEmailPopup(root));
                    emailStages.get(emailStages.size() - 1).setOnCloseRequest(this::popStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
            case "replyAll"://args[1] = x
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    while (args[2].contains(",")){
                        newEmailController.setRecipient(args[2].substring(0, args[2].indexOf(",")));
                        args[2] = args[2].substring(args[2].indexOf(",") + 1);
                    }
                    newEmailController.setSubject("Re: " + args[3]);
                    newEmailController.setBody("\"" + args[4] + "\"");

                    emailStages.add(newEmailController.showNewEmailPopup(root));
                    emailStages.get(emailStages.size() - 1).setOnCloseRequest(this::popStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
        }
    }


    public void openShowEmailPopup(Email email, Parent root) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("showEmail.fxml"));
            root = loader.load();

            showEmailController showEmailController = loader.getController();
            showEmailController.showEmailPopup(email);

            emailStages.add(showEmailController.showEmailPopup(email, root));
        } catch (Exception e) {
            System.out.println("Error opening popup");
        }
    }
    /**
     * Load contacts from a CSV file
     */
    public void loadContacts() {
        try {
            File contactsFile = new File(getClass().getResource("contacts.csv").getFile());
            Scanner scanner = new Scanner(contactsFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                contacts.add(new Contact(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        for (Contact contact : contacts) {
            contactsList.getItems().add(contact.getEmail());
        }
    }

    public void handleCloseRequest(javafx.stage.WindowEvent event) {
        if (emailStages.size() != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Client has open emails");
            alert.setContentText("Are you sure you want to stop the client? Active emails will be closed.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (Stage stage : emailStages) {
                    stage.close();
                }
                System.exit(0);
            } else {
                event.consume();
            }
        } else {
            System.exit(0);
        }
    }

    public void handleEmail(Email email) {
        emails.put(email.getId(), email);
        senderList.getItems().add(new EmailItem(email.getSender(), email.getId()));
        subjectList.getItems().add(new EmailItem(email.getSubject(), email.getId()));
        bodyList.getItems().add(new EmailItem(email.getBody(), email.getId()));
    }

    public void popStage(javafx.stage.WindowEvent event) {
        emailStages.remove(event.getSource());
    }

    public void setOwner(Contact owner) {
        this.owner = owner;
    }

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

    private void bindScrollBars(ScrollBar sb1, ScrollBar sb2) {
        sb1.valueProperty().bindBidirectional(sb2.valueProperty());
    }
}
