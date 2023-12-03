package com.client.client.utils.specificHandlers;

import com.client.client.models.Email;
import com.client.client.models.Packet;
import com.client.client.utils.AccountUtils;

import java.util.HashMap;

public class DeleteHandler {
    public Packet handleDelete(int id, String username) {
        String accountFolder = "client/src/main/resources/com/client/client/accounts/" + username;
        HashMap<Integer, Email> emails;
        if(AccountUtils.doesAccountExist(username)){
            return new Packet("failed", "Account does not exist", "client");
        }
        emails = AccountUtils.retrieveEmails(accountFolder);
        if(emails==null){
            return new Packet("failed", "Could not retrieve emails", "client");
        }
        if(emails.containsKey(id)){
            emails.remove(id);
            return new Packet("successful", "email deleted", "client");
        } else {
            return new Packet("failed", "Could not retrieve emails", "client");
        }
    }
}
