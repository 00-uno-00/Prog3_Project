package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

public class LoginHandler {

    public Packet login(String username) {
        Packet responsePacket;
        if (Email.isValidFormat(username)) {
            responsePacket = new Packet("failed", "invalid username format", "server");
            return responsePacket;
        }
        String accountFolder = "Server/src/main/resources/com/server/server/accounts/" + username;
        if (AccountUtils.doesAccountExist(accountFolder)) {
            responsePacket = new Packet("failed", "username does not exist", "server");
            return responsePacket;
        }
        if (!AccountUtils.isAccountInitialized(accountFolder)) {
            responsePacket = new Packet("failed", "account not initialized", "server");
            return responsePacket;
        }
        responsePacket = new Packet("successful", "logged in", "server");
        return responsePacket;
    }
}