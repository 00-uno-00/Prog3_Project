package com.server.server.utils.specificHandlers;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.AccountUtils;

/**
 * Class responsible for handling login requests.
 */
public class LoginHandler {

    /**
     * Method to log in a user.
     * Validates the username, checks if it exists and if the account is initialized.
     * @param username The username to log in
     * @return a Packet indicating the result of the login
     */
    public Packet login(String username) {
        Packet responsePacket;

        // Check if the username is in a valid email format
        if (!Email.isValidFormat(username)) {
            responsePacket = new Packet("failed", "invalid username format", "server");
            return responsePacket;
        }

        // Check if the username exists
        String accountFolder = "Server/src/main/resources/com/server/server/accounts/" + username;
        if (AccountUtils.doesAccountExist(accountFolder)) {
            responsePacket = new Packet("failed", "username does not exist", "server");
            return responsePacket;
        }

        // Check if the account is initialized
        if (!AccountUtils.isAccountInitialized(accountFolder)) {
            responsePacket = new Packet("failed", "account not initialized", "server");
            return responsePacket;
        }

        // If all the above checks pass, the login is successful
        responsePacket = new Packet("successful", "logged in", "server");
        return responsePacket;
    }
}