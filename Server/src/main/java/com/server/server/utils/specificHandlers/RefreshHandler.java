package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handler class for refreshing emails.
 */
public class RefreshHandler {

    /**
     * Refreshes the emails for a given user.
     * @param username the username of the user
     * @param presentIds the list of email IDs that the user already has
     * @return a Packet containing the status of the operation and the list of new emails
     */
    public Packet refresh(String username, List<Integer> presentIds) {
        String accountFolder = AccountUtils.getAccountFolder(username);
        HashMap<Integer, Email> emails;
        List<Email> emailList;

        // Check if the account exists
        if(!AccountUtils.doesAccountExist(accountFolder)){
            return new Packet("failed", "Account does not exist", "server");
        }

        // Retrieve the emails from the account
        try {
            emails = AccountUtils.retrieveEmails(accountFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Check if the emails were retrieved successfully
        if(emails.isEmpty()) {
            return new Packet("successful", new ArrayList<Email>(), "server");
        } else {
            emailList = new ArrayList<>(emails.values());
        }

        List<Email> neededEmails = new ArrayList<>();

        // If there are no present IDs, return all emails
        if(presentIds.isEmpty()){
            return new Packet("successful", emailList, "server");
        }

        // Check each email to see if it's ID is in the list of present IDs
        // If it's not, add it to the list of needed emails
        for(Email email : emailList){
            if(!presentIds.contains(email.getId())){
                neededEmails.add(email);
            }
        }

        // Return the list of needed emails
        return new Packet("successful", neededEmails, "server");
    }
}