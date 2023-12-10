package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RefreshHandler {
    public Packet refresh(String username, List<Integer> presentIds) {
        String accountFolder = AccountUtils.getAccountFolder(username);
        HashMap<Integer, Email> emails;
        List<Email> emailList;
        if(AccountUtils.doesAccountExist(accountFolder)){
            return new Packet("failed", "Account does not exist", "server");
        }
        emails = AccountUtils.retrieveEmails(accountFolder);
        if(emails.isEmpty()) {
            return new Packet("successful", new ArrayList<Email>(), "server");
        } else {
         emailList = new ArrayList<>(emails.values());
        }
        List<Email> neededEmails = new ArrayList<>();
        //foreach email in emails check if it's id is present in presentIds, if not add it to neededEmails
        if(presentIds.isEmpty()){
            return new Packet("successful", emailList, "server");
        }
        for(Email email : emailList){
            if(!presentIds.contains(email.getId())){
                neededEmails.add(email);
            }
        }
        return new Packet("successful", neededEmails, "server");
    }
}
