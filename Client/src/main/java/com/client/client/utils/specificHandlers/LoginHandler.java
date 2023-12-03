package com.client.client.utils.specificHandlers;

import com.client.client.models.Email;
import com.client.client.models.Packet;
import com.client.client.utils.AccountUtils;

public class LoginHandler {

    public Packet login(String username) {
        Packet responsePacket;
        if (Email.isValidFormat(username)) {
            responsePacket = new Packet("failed", "invalid username format", "client");
            return responsePacket;
        }
        String accountFolder = "client/src/main/resources/com/client/client/accounts/" + username;
        if (AccountUtils.doesAccountExist(accountFolder)) {
            responsePacket = new Packet("failed", "username does not exist", "client");
            return responsePacket;
        }
        if (!AccountUtils.isAccountInitialized(accountFolder)) {
            responsePacket = new Packet("failed", "account not initialized", "client");
            return responsePacket;
        }
        responsePacket = new Packet("successful", "logged in", "client");
        return responsePacket;
    }
}