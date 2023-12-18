package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Handler for reading emails
 */
public class ReadHandler {

    /**
     * Reads an email
     * @param id the id of the email to read
     * @param username the username of the account to read the email from
     * @return a packet containing the result of the operation
     */
     public Packet read(int id, String username) {
        String accountFolder = AccountUtils.getAccountFolder(username);
        HashMap<Integer, Email> emails;

        if(!AccountUtils.doesAccountExist(username)){
            return new Packet("failed", "Account does not exist", "server");
        }

        try {
            emails = AccountUtils.retrieveEmails(accountFolder);
        } catch (IOException e) {
            return new Packet("failed", "Could not retrieve emails", "server");
        }
        if(emails.containsKey(id)){
            Email email = emails.get(id);
            email.setRead(true);
            emails.put(id, email);
            AccountUtils.storeEmails(accountFolder, emails);
            return new Packet("successful", "email read", "server");
        } else {
            return new Packet("failed", "Could not retrieve emails", "server");
        }
    }
}