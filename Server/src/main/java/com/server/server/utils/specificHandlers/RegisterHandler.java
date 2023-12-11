package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

/**
 * Class responsible for handling registration requests.
 */
public class RegisterHandler {

    /**
     * Method to register a new user.
     * Validates the username, checks if it's already taken, creates and initializes the account folder.
     * @param username The username to register
     * @return a Packet indicating the result of the registration
     */
    public Packet register(String username) {
        Packet responsePacket;

        // Check if the username is in a valid email format
        if (Email.isValidFormat(username)) {
            responsePacket = new Packet("failed", "invalid username format", "server");
            return responsePacket;
        }

        // Check if the username is already taken
        String accountFolder = "Server/src/main/resources/com/server/server/accounts/" + username;
        if (!AccountUtils.isAlreadyTaken(accountFolder)) {
            responsePacket = new Packet("failed", "username already taken", "server");
            return responsePacket;
        }

        // Create the account folder
        if(!AccountUtils.createAccountFolder(accountFolder)) {
            responsePacket = new Packet("failed", "error creating account folder", "server");
            return responsePacket;
        }

        // Initialize the account folder
        if(!AccountUtils.initializeAccountFolder(accountFolder)){
            responsePacket = new Packet("failed", "error initializing account folder", "server");
            return responsePacket;
        }

        // If all the above checks pass, the registration is successful
        responsePacket = new Packet("successful", "registered", "server");
        return responsePacket;
    }
}