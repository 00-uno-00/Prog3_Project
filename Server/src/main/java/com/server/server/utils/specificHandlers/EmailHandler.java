package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;

import com.server.server.utils.AccountUtils;

import java.util.HashMap;
import java.util.List;

/*
Email handling logic :
1) Verify if sender was valid (moved to PacketUtils.isValidSender)
2) Verify if recipient was valid
     -> If valid: step 3
     -> If !valid: write Email to sender folder ("recipient not valid. Sincerely, the Server")
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
            //  "Server/src/main/resources/com/server/server/accounts"
            if(AccountUtils.doesAccountExist(emailAddress)){
                return false;
            }
        }
        return true;
    }
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

    private Packet writeEmailToMultiple(Email email, List<String> usernames) {
        for (String username : usernames) {
            Packet responsePacket = writeEmail(email, username);
            if (!responsePacket.getOperation().equals("successful")) {
                return responsePacket;
            }
        }
        return new Packet("successful", "emails written", "server");
    }

    public Packet handleEmail(Email mail, List<String> usernames) {
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