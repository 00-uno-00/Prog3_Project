package com.client.client.utils.specificHandlers;

import com.client.client.models.Email;
import com.client.client.models.Packet;
import com.client.client.utils.AccountUtils;

public class RegisterHandler {

    public Packet register(String username) {
        Packet responsePacket;

        if (Email.isValidFormat(username)) {
            responsePacket = new Packet("failed", "invalid username format", "client");
            return responsePacket;
        }
        String accountFolder = "client/src/main/resources/com/client/client/accounts/" + username;
        if (!AccountUtils.isAlreadyTaken(accountFolder)) {
            responsePacket = new Packet("failed", "username already taken", "client");
            return responsePacket;
        }
        if(!AccountUtils.createAccountFolder(username)) {
            responsePacket = new Packet("failed", "error creating account folder", "client");
            return responsePacket;
        }
        if(!AccountUtils.initializeAccountFolder(username)){
            responsePacket = new Packet("failed", "error initializing account folder", "client");
            return responsePacket;
        }
        responsePacket = new Packet("successful", "registered", "client");
        return responsePacket;
    }
}
