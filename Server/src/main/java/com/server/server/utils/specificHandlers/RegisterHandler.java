package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;

import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

public class RegisterHandler {

    public Packet register(String username) {
        Packet responsePacket;

        if (Email.isValidFormat(username)) {
            responsePacket = new Packet("failed", "invalid username format", "server");
            return responsePacket;
        }
        String accountFolder = "Server/src/main/resources/com/server/server/accounts/" + username;
        if (!AccountUtils.isAlreadyTaken(accountFolder)) {
            responsePacket = new Packet("failed", "username already taken", "server");
            return responsePacket;
        }
        if(!AccountUtils.createAccountFolder(accountFolder)) {
            responsePacket = new Packet("failed", "error creating account folder", "server");
            return responsePacket;
        }
            if(!AccountUtils.initializeAccountFolder(accountFolder)){
            responsePacket = new Packet("failed", "error initializing account folder", "server");
            return responsePacket;
        }
        responsePacket = new Packet("successful", "registered", "server");
        return responsePacket;
    }
}
