package com.server.server.utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.server.server.models.Email;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AccountUtils {

    public static boolean isAlreadyTaken(String accountFolder){
        // check if the username String is equal to
        // the name of a folder in the directory:
        //  "Server/src/main/resources/com/server/server/accounts"
        // if it is, then the username is already taken
        return !(new File(accountFolder).exists());
    }

    public static boolean createAccountFolder(String accountFolder) {
        File directory = new File(accountFolder);
        return directory.mkdirs();
    }

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
     * Checks if the account folder exists
     * @param accountFolder the path to the account folder
     * @return true if the account folder does not exist
     */
    public static boolean doesAccountExist(String accountFolder){ //TODO invert logic
        return !new File(accountFolder).exists();
    }

    public static boolean isAccountInitialized(String accountFolder){
        // check if the username String is equal to
        // the name of a folder in the directory:
        //  "Server/src/main/resources/com/server/server/accounts"
        // if it is, then the username is already taken
        return new File(accountFolder + "/emails.json").exists();
    }

    public static HashMap<Integer, Email> retrieveEmails(String accountFolder) {
        String emailFile = accountFolder + "/emails.json";
        HashMap<Integer, Email> emails = new HashMap<>();
        Gson gson = new Gson();

        // Step 1: Retrieve the emails.json file from the sender's folder
        try (FileReader reader = new FileReader(emailFile)) {
            // Step 2: Convert the JSON back into HashMap<ID, Email>
            Type type = new TypeToken<HashMap<Integer, Email>>(){}.getType();
            emails = gson.fromJson(reader, type);
        } catch (Exception e) {
            System.err.println("Error reading emails.json: " + e.getMessage());
            return emails;
        }
        return emails;
    }

    public static boolean storeEmails(String accountFolder, HashMap<Integer, Email> emails) {
        String emailFile = accountFolder + "/emails.json";
        Gson gson = new Gson();

        // Step 1: Convert the HashMap back into JSON
        String json = gson.toJson(emails);

        // Step 2: Write the JSON back into the emails.json file
        try (FileWriter writer = new FileWriter(emailFile)) {
            writer.write(json);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to emails.json: " + e.getMessage());
            return false;
        }
    }

    public static String getAccountFolder(String username){
        return "Server/src/main/resources/com/server/server/accounts/" + username;
    }
}
