package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.util.HashMap;

/**
 * Class responsible for handling delete requests.
 */
public class DeleteHandler {

    /**
     * Method to delete an email from a user's account.
     * Retrieves the user's emails, removes the email with the specified id, and stores the updated list of emails.
     * @param id The id of the email to delete
     * @param username The username of the account to delete the email from
     * @return a Packet indicating the result of the operation
     */
    public Packet delete(int id, String username) {
        String accountFolder = AccountUtils.getAccountFolder(username);
        HashMap<Integer, Email> emails;

        // Check if the account exists
        if(AccountUtils.doesAccountExist(username)){
            return new Packet("failed", "Account does not exist", "server");
        }

        // Retrieve the emails
        emails = AccountUtils.retrieveEmails(accountFolder);
        if(emails==null){
            return new Packet("failed", "Could not retrieve emails", "server");
        }

        // Check if the email with the specified id exists, and delete it if it does
        if(emails.containsKey(id)){
            emails.remove(id);
            return new Packet("successful", "email deleted", "server");
        } else {
            return new Packet("failed", "Could not retrieve emails", "server");
        }
    }
}