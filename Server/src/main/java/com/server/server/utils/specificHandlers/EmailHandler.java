package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Class responsible for handling email operations.
 */
public class EmailHandler {

    /**
     * Method to validate the recipient of an email.
     * Checks if the recipient is not null or empty, and if it corresponds to an existing account.
     * @param recipient The recipient to validate
     * @return true if the recipient is valid, false otherwise
     */
    private boolean isValidRecipient(List<String> recipient) {
        if (recipient == null || recipient.isEmpty()) {
            return false;
        }
        for (String emailAddress : recipient) {
            // check if the recipient string is equal to
            // the name of a folder in the directory:
            //  "Server/src/main/resources/com/server/server/accounts"
            if(AccountUtils.doesAccountExist(emailAddress)){
                return false;
            }
        }
        return true;
    }

    /**
     * Method to write an email to a user's account.
     * Retrieves the user's emails, adds the new email, and stores the updated list of emails.
     * @param mail The email to write
     * @param username The username of the account to write the email to
     * @return a Packet indicating the result of the operation
     */
    private Packet writeEmail(Email mail, String username) {
        String accountFolder = "Server/src/main/resources/com/server/server/accounts/" + username;
        HashMap<Integer, Email> emails;
        emails = AccountUtils.retrieveEmails(accountFolder);

        if (emails != null) {
            emails.put(mail.getId(), mail);
        } else {
            return new Packet("failed", "Could not retrieve emails", "server");
        }

        boolean sent = AccountUtils.storeEmails(accountFolder, emails);

        if(sent){
            return new Packet("successful", "email written", "server");
        } else {
            return new Packet("failed", "Could not store emails", "server");
        }
    }

    /**
     * Method to write an email to multiple users' accounts.
     * Calls writeEmail for each username in the list.
     * @param email The email to write
     * @param usernames The list of usernames to write the email to
     * @return a Packet indicating the result of the operation
     */
    private Packet writeEmailToMultiple(Email email, List<String> usernames) {
        for (String username : usernames) {
            Packet responsePacket = writeEmail(email, username);
            if (!responsePacket.getOperation().equals("successful")) {
                return responsePacket;
            }
        }
        return new Packet("successful", "emails written", "server");
    }

    /**
     * Method to handle an email operation.
     * Validates the recipient, and calls either writeEmail or writeEmailToMultiple depending on the number of recipients.
     * @param mail The email to handle
     * @param usernames The list of usernames to send the email to
     * @return a Packet indicating the result of the operation
     */
    public Packet email(Email mail, List<String> usernames) {
        if (!isValidRecipient(mail.getRecipient())) {
            return new Packet("failed", "invalid recipient", "server");
        }
        if(usernames.size()>1){
            return writeEmailToMultiple(mail, usernames);
        } else {
            return writeEmail(mail, usernames.get(0));
        }
    }
}