package com.client.client.utils.specificHandlers;

import com.client.client.models.Email;
import com.client.client.models.Packet;
import com.client.client.utils.AccountUtils;

import java.util.HashMap;
import java.util.List;

/*
Email handling logic :
1) Verify if sender was valid (moved to PacketUtils.isValidSender)
2) Verify if recipient was valid
     -> If valid: step 3
     -> If !valid: write Email to sender folder ("recipient not valid. Sincerely, the client")
         |-> Send "not sent" Packet to client
3) Write Email file into recipient folder
4) Send "confirmation" Packet to client
*/
public class EmailHandler {
    private boolean isValidRecipient(List<String> recipient) {
        if (recipient == null || recipient.isEmpty()) {
            return false;
        }
        for (String emailAddress : recipient) {
            // check if the recipient string is equal to
            // the name of a folder in the directory:
            //  "client/src/main/resources/com/client/client/accounts"
            if(AccountUtils.doesAccountExist(emailAddress)){
                return false;
            }
        }
        return true;
    }
    private Packet writeEmail(Email mail, String username) {
        String accountFolder = "client/src/main/resources/com/client/client/accounts/" + username;
        HashMap<Integer, Email> emails;
        emails = AccountUtils.retrieveEmails(accountFolder);

        if (emails != null) {
            emails.put(mail.getId(), mail);
        } else {
            return new Packet("failed", "Could not retrieve emails", "client");
        }

        boolean sent = AccountUtils.storeEmails(accountFolder, emails);

        if(sent){
            return new Packet("successful", "email written", "client");
        } else {
            return new Packet("failed", "Could not store emails", "client");
        }
    }

    private Packet writeEmailToMultiple(Email email, List<String> usernames) {
        for (String username : usernames) {
            Packet responsePacket = writeEmail(email, username);
            if (!responsePacket.getOperation().equals("successful")) {
                return responsePacket;
            }
        }
        return new Packet("successful", "emails written", "client");
    }

    public Packet handleEmail(Email mail, List<String> usernames) {
        if (!isValidRecipient(mail.getRecipient())) {
            return new Packet("failed", "invalid recipient", "client");
        }
        if(usernames.size()>1){
            return writeEmailToMultiple(mail, usernames);
        } else {
            return writeEmail(mail, usernames.get(0));
        }
    }
}