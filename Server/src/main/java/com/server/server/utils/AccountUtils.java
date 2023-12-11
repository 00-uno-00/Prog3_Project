package com.server.server.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.server.server.models.Email;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Utility class for handling account related operations.
 */
public class AccountUtils {

    /**
     * Checks if the account is already taken.
     * @param accountFolder the path to the account folder
     * @return true if the account is already taken
     */
    public static boolean isAlreadyTaken(String accountFolder){
        return !(new File(accountFolder).exists());
    }

    /**
     * Creates a new account folder.
     * @param accountFolder the path to the account folder
     * @return true if the directory was created successfully
     */
    public static boolean createAccountFolder(String accountFolder) {
        File directory = new File(accountFolder);
        return directory.mkdirs();
    }

    /**
     * Initializes a new account folder with an empty emails.json file.
     * @param accountFolder the path to the account folder
     * @return true if the initialization was successful
     */
    public static boolean initializeAccountFolder(String accountFolder) {
        HashMap<Integer, Email> emails = new HashMap<>();
        Gson gson = new Gson();
        String json = gson.toJson(emails);

        try (FileWriter file = new FileWriter(accountFolder + "/emails.json")) {
            file.write(json);
            return true;
        } catch (IOException e) {
            System.err.println("Error initializing account folder: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the account folder exists.
     * @param accountFolder the path to the account folder
     * @return true if the account folder does not exist
     */
    public static boolean doesAccountExist(String accountFolder){
        return !new File(accountFolder).exists();
    }

    /**
     * Checks if the account is initialized.
     * @param accountFolder the path to the account folder
     * @return true if the account is initialized
     */
    public static boolean isAccountInitialized(String accountFolder){
        return new File(accountFolder + "/emails.json").exists();
    }

    /**
     * Retrieves the emails from the account folder.
     * @param accountFolder the path to the account folder
     * @return a HashMap of emails
     */
    public static HashMap<Integer, Email> retrieveEmails(String accountFolder) {
        String emailFile = accountFolder + "/emails.json";
        HashMap<Integer, Email> emails = new HashMap<>();
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(emailFile)) {
            Type type = new TypeToken<HashMap<Integer, Email>>(){}.getType();
            emails = gson.fromJson(reader, type);
        } catch (Exception e) {
            System.err.println("Error reading emails.json: " + e.getMessage());
            return emails;
        }
        return emails;
    }

    /**
     * Stores the emails in the account folder.
     * @param accountFolder the path to the account folder
     * @param emails the emails to be stored
     * @return true if the emails were stored successfully
     */
    public static boolean storeEmails(String accountFolder, HashMap<Integer, Email> emails) {
        String emailFile = accountFolder + "/emails.json";
        Gson gson = new Gson();
        String json = gson.toJson(emails);

        try (FileWriter writer = new FileWriter(emailFile)) {
            writer.write(json);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to emails.json: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the account folder path.
     * @param username the username of the account
     * @return the path to the account folder
     */
    public static String getAccountFolder(String username){
        return "Server/src/main/resources/com/server/server/accounts/" + username;
    }
}