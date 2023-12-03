package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.util.HashMap;

public class DeleteHandler {
    public Packet handleDelete(int id, String username) {
        String accountFolder = "Server/src/main/resources/com/server/server/accounts/" + username;
        HashMap<Integer, Email> emails;
        if(AccountUtils.doesAccountExist(username)){
            return new Packet("failed", "Account does not exist", "server");
        }
        emails = AccountUtils.retrieveEmails(accountFolder);
        if(emails==null){
            return new Packet("failed", "Could not retrieve emails", "server");
        }
        if(emails.containsKey(id)){
            emails.remove(id);
            return new Packet("successful", "email deleted", "server");
        } else {
            return new Packet("failed", "Could not retrieve emails", "server");
        }
    }
}
