package com.client.client.controllers;

import com.client.client.ClientApplication;
import com.client.client.models.ClientModel;
import com.server.server.models.Email;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//TODO contacts don't load properly

public class ClientController implements Initializable{

    @FXML
    private Button changeAccount;

    @FXML
    private Button refresh;

    @FXML
    private Button newEmailButton;

    @FXML
    private ListView<String> contactsList;

    @FXML
    private ListView<EmailItem> senderList;

    @FXML
    private ListView<EmailItem> subjectList;

    @FXML
    private ListView<EmailItem> bodyList;

    private final List<String> contacts = new ArrayList<>();//move to set?

    private final HashMap<Integer, Email> emails = new HashMap<>();

    private final List<Stage> emailStages = new ArrayList<>();

    Parent root;
    Scene scene;
    Stage stage;

    private ClientModel model;
    private ScheduledExecutorService executorService;

    public void setModel(ClientModel model) {
        this.model = model;
    }

    //the FXML uses this by default
    public ClientController() {
        this.model = new ClientModel();
    }

    private String owner = ""; // must be initialized at startup

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactsList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            openNewEmailPopup(new String[]{"new", "1", contactsList.getSelectionModel().getSelectedItem(), "", ""});
        });

        newEmailButton.onActionProperty().setValue(actionEvent -> {
            openNewEmailPopup(new String[]{"new", "1", "", "", ""});
        });

        handleSelectionChange(senderList, emails);
        handleSelectionChange(subjectList, emails);
        handleSelectionChange(bodyList, emails);

        Platform.runLater(() -> {
            ScrollBar senderScrollBar = getVerticalScrollbar(senderList);
            ScrollBar subjectScrollBar = getVerticalScrollbar(subjectList);
            ScrollBar bodyScrollBar = getVerticalScrollbar(bodyList);

            bindScrollBars(senderScrollBar, bodyScrollBar);
            bindScrollBars(bodyScrollBar, subjectScrollBar);
            bindScrollBars(subjectScrollBar, senderScrollBar);
        });

        // Set the cell factory for each ListView
        setCellFactoryForListView(senderList);
        setCellFactoryForListView(subjectList);
        setCellFactoryForListView(bodyList);
    }

    private void setCellFactoryForListView(ListView<EmailItem> listView) {
        listView.setCellFactory(lv -> new ListCell<EmailItem>() {
            @Override
            protected void updateItem(EmailItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-font-weight: normal;");
                } else {
                    setText(item.toString());
                    if (!emails.get(item.id()).isRead()) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-font-weight: normal;");
                    }
                }
            }
        });
    }

    private void handleSelectionChange(ListView<EmailItem> listView, HashMap<Integer, Email> emails) {
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            EmailItem selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Email selectedEmail = emails.get(selectedItem.id());
                if (selectedEmail != null) {
                    Email newEmailVersion = openShowEmailPopup(selectedEmail);
                    if (newEmailVersion != selectedEmail){
                        emails.replace(selectedItem.id(), newEmailVersion);
                    }
                    senderList.refresh();
                    subjectList.refresh();
                    bodyList.refresh();
                }
            }
        });
    }

    public void changeAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/client/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setTitle("Login");
            newStage.setScene(scene);
            // Load the Client icon
            Image icon = new Image(Objects.requireNonNull(ClientApplication.class.getResource("icons/mail.png")).toExternalForm());
            // Set the icon for the stage
            newStage.getIcons().add(icon);
            newStage.show();

            // Shutdown the executor service
            if (executorService != null) {
                executorService.shutdown();
            }

            // Close the current stage
            Stage currentStage = (Stage) changeAccount.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.out.println("Error opening login popup");
        }
    }

    public void receiveAll(){
        refresh(false);
    }

    public void refresh(boolean automatic) {
        List<Email> refreshedEmails = model.refresh(new ArrayList<>(emails.keySet()));
        loadContacts(); //update contacts
        int numberOfNewEmails = 0;
        if (refreshedEmails != null && !refreshedEmails.isEmpty()) {
            //order emails by date in reverse order
            refreshedEmails.sort(Comparator.comparing(Email::getDate));
            for (Email email : refreshedEmails) {
                if (Objects.equals(email.getSender(), "Server Offline")) {
                    if (!automatic) {
                        showServerOfflineAlert();
                    }
                } else {
                    handleEmail(email);
                    if(!email.isRead()) {
                        numberOfNewEmails++;
                    }
                }
            }
            if (numberOfNewEmails > 0) {
                showNewEmailsAlert(numberOfNewEmails);
            }
        } else if (!automatic) {
            showNoNewEmailsAlert();
        }
    }

    public void showNoNewEmailsAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No New Emails");
        alert.setHeaderText(null);
        alert.setContentText("There are no new emails.");
        alert.showAndWait();
    }

    public void showNewEmailsAlert(int numberOfNewEmails){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Emails");
        alert.setHeaderText(null);
        if(numberOfNewEmails == 1) {
            alert.setContentText("There is 1 new email.");
        } else {
            alert.setContentText("There are " + numberOfNewEmails + " new emails.");
        }
        alert.showAndWait();
    }

    public void showServerOfflineAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Could not reach the Server");
        alert.setContentText("Connection error.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    //can be changed into ViewClass
    /**
     * Opens a new email popup
     * @param args args[0] is the type of new mail, args[1] is the number of recipients, args[2] is the recipient(s), args[3] is the subject, args[4] is the body
     */
    public void openNewEmailPopup(String[] args) {
        switch (args[0]){
            case "new"://args[1] = 1
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/client/controllers/newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    newEmailController.setRecipient(args[2]);
                    newEmailController.setSubject(args[3]);
                    newEmailController.setBody(args[4]);
                    newEmailController.setOwner(owner);

                    Stage newEmailStage = newEmailController.showNewEmailPopup(root, this);
                    newEmailStage.setOnCloseRequest(this::popStage);

                    emailStages.add(newEmailStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
            case "reply"://args[1] = 1
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/client/controllers/newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    newEmailController.setRecipient(args[2]);
                    newEmailController.setSubject("Re: " + args[3]);
                    newEmailController.setBody("\"" + args[4] + "\"");
                    newEmailController.setOwner(owner);

                    Stage newEmailStage = newEmailController.showNewEmailPopup(root, this);
                    newEmailStage.setOnCloseRequest(this::popStage);

                    emailStages.add(newEmailStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
            case "forward"://args[1] = 1
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/client/controllers/newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    newEmailController.setRecipient(args[2]);
                    newEmailController.setSubject("Fwd: " + args[3]);
                    newEmailController.setBody("\"" + args[4] + "\"");
                    newEmailController.setOwner(owner);

                    Stage newEmailStage = newEmailController.showNewEmailPopup(root, this);
                    newEmailStage.setOnCloseRequest(this::popStage);

                    emailStages.add(newEmailStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
            case "replyAll"://args[1] = x
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/client/controllers/newEmail.fxml"));
                    root = loader.load();

                    newEmailController newEmailController = loader.getController();
                    while (args[2].contains(",")){
                        newEmailController.setRecipient(args[2].substring(0, args[2].indexOf(",")));
                        args[2] = args[2].substring(args[2].indexOf(",") + 1);
                    }
                    newEmailController.setSubject("Re: " + args[3]);
                    newEmailController.setBody("\"" + args[4] + "\"");
                    newEmailController.setOwner(owner);

                    Stage newEmailStage = newEmailController.showNewEmailPopup(root, this);
                    newEmailStage.setOnCloseRequest(this::popStage);

                    emailStages.add(newEmailStage);
                } catch (Exception e) {
                    System.out.println("Error opening popup");
                }
                break;
        }
    }


    public Email openShowEmailPopup(Email email) {
        try {
            if(!email.isRead()) {
                String response = model.read(email.getId());
                if (response.equals("successful")) {
                    email.markAsRead();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Read Error");
                    alert.setContentText("There was an error reading the email: " + response);
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
            //show email popup anyway, even if there was an error reading the email
            // Initialize the Label for the date of the Email popup
            ArrayList<ListView<EmailItem>> showEmailList = new ArrayList<>();
            showEmailList.add(senderList);
            showEmailList.add(subjectList);
            showEmailList.add(bodyList);
            showEmailController showEmailController = new showEmailController();
            showEmailController.showEmailPopup(email,this).setOnCloseRequest(event -> {deselectList(showEmailList);});

        } catch (Exception e) {
            System.out.println("Error opening popup");
        }
        return email;
    }
    /**
     * Load contacts from a CSV file
     */
    public void loadContacts() {
        try {
            URL contactsFile = getClass().getResource("/" + owner + "Contacts.csv");
            if (contactsFile == null) {
                File newFile = new File(owner + "Contacts.csv");//owner is null when creating the file
                try {
                    if (newFile.createNewFile()) {
                        System.out.println("File created: " + newFile.getName());
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while creating the file.");
                    System.err.println("Error: " + e + e.getCause());
                }
                contactsFile = newFile.toURI().toURL();
            }
            Scanner scanner = new Scanner(new File(contactsFile.getFile()));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                contacts.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating or reading the file");
            System.err.println("Error: " + e + e.getCause());
        }

        for (String contact : contacts) {
            if (!contactsList.getItems().contains(contact)) {
                contactsList.getItems().add(contact);
            }
        }
    }

    public void addRecipientToContacts(List<String> recipients) {
        for (String recipient : recipients) {
            if (!contacts.contains(recipient)) {
                contacts.add(recipient);
                try {
                    FileWriter writer = new FileWriter(owner + "Contacts.csv", true);
                    writer.write(recipient + "\n");
                    writer.close();
                } catch (IOException e) {
                    System.out.println("An error occurred while writing to contacts.csv");
                }
            }
        }
    }

    public void handleCloseRequest(javafx.stage.WindowEvent event) {
        if (!emailStages.isEmpty()) {
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


    public void deleteEmail(Email email, Stage stage) {
        String response = model.delete(email.getId());
        if (response.equals("successful")){
            emails.remove(email.getId());

            deselectList(new ArrayList<>(Arrays.asList(senderList, subjectList, bodyList)));

            senderList.getItems().remove(new EmailItem(email.getSender(), email.getId()));
            subjectList.getItems().remove(new EmailItem(email.getSubject(), email.getId()));
            bodyList.getItems().remove(new EmailItem(email.getBody(), email.getId()));

            senderList.getItems().removeIf(item -> item.id() == email.getId());
            subjectList.getItems().removeIf(item -> item.id() == email.getId());
            bodyList.getItems().removeIf(item -> item.id() == email.getId());

            stage.close();
            operationSuccess("Delete");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Delete Error");
            alert.setContentText("There was an error deleting the email: " + response);
            Optional<ButtonType> result = alert.showAndWait();
        }

    }

    public boolean sendEmail(Email email) {
        String response = model.send(email);
        if (response.equals("successful")) {
            operationSuccess("Send");
            addRecipientToContacts(email.getRecipients());
            loadContacts();
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Send Error");
            alert.setContentText("There was an error sending the email: " + response);
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
    }

    public void forwardEmail(Email email) {
        openNewEmailPopup(new String[]{"forward", "1", "", email.getSubject(), email.getBody()});
    }

    public void replyEmail(Email email) {
        openNewEmailPopup(new String[]{"reply", "1", email.getSender(), email.getSubject(), email.getBody()});
    }
    public void handleEmail(Email email) {
        emails.put(email.getId(), email);
        //Passing 0 as index to add the element at the beginning of the list
        senderList.getItems().add(0, new EmailItem(email.getSender(), email.getId()));
        subjectList.getItems().add(0, new EmailItem(email.getSubject(), email.getId()));
        bodyList.getItems().add(0, new EmailItem(email.getBody(), email.getId()));
    }

    public void popStage(javafx.stage.WindowEvent event) {
        emailStages.remove((Stage) event.getSource());
    }

    public void setOwner(String owner) {
        this.owner = owner;
        loadContacts(); //load constacts after setting owner or elese createws a generic contacts file
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
        if (sb1 != null && sb2 != null) {
            sb1.valueProperty().bindBidirectional(sb2.valueProperty());
        }
    }

    private <E> void deselectList(ArrayList<ListView<E>> listView)  {
        for (ListView<?> list : listView) {
            if (list.getSelectionModel().getSelectedItem() != null) {
                list.getSelectionModel().clearSelection();
            }
        }
    }

    private void operationSuccess(String operation){
        Tooltip tooltip = new Tooltip(operation + " successful");
        tooltip.setAutoHide(true);

        Scene scene = senderList.getScene();
        Window window = scene.getWindow();

        double xPosition = window.getX() + window.getWidth() / 2;
        double yPosition = window.getY() + window.getHeight() / 2;

        tooltip.show(senderList, xPosition, yPosition);
    }

    public void startScheduledRefresh() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                Platform.runLater(() -> {
                    refresh(true);
                });
            } catch (Exception e) {
                System.err.println("Error in scheduled task: " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    
}
