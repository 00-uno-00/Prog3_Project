package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.util.HashMap;
import java.util.List;

public class RefreshHandler {
    public Packet refresh(String username, List<Integer> presentIds) {
        String accountFolder = AccountUtils.getAccountFolder(username);
        HashMap<Integer, Email> emails;
        if(AccountUtils.doesAccountExist(username)){
            return new Packet("failed", "Account does not exist", "server");
        }
        emails = AccountUtils.retrieveEmails(accountFolder);
        List<Email> neededEmails;
        //TODO cycle through emails and if the email is not in presentIds, add it to neededEmails
        return new Packet("successful", emails, "server");
    }
}
