package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.specificHandlers.LoginHandler;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;

import java.io.ObjectOutputStream;

/**
 * This class implements the PacketHandlerStrategy interface and provides a specific strategy to handle login packets.
 */
public class LoginStrategy implements PacketHandlerStrategy {

    /**
     * This method handles the incoming packet and performs the necessary operations based on the packet's payload.
     * If the payload is a string, it logs the login request, creates a new LoginHandler, and calls its login method.
     * If the payload is not a string, it logs an error message and returns without doing anything.
     * After handling the packet, it sends a response packet back to the sender.
     *
     * @param packet The incoming packet to be handled.
     * @param objectOutputStream The ObjectOutputStream to send the response packet.
     * @param logger The logger to log the operations.
     */
    @Override
    public boolean handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        boolean result = false;
        if(packet.getPayload() instanceof String username){
            logger.log("Received login request from : " + username, "Login" );
            LoginHandler loginHandler = new LoginHandler();
            responsePacket = loginHandler.login(username);
        } else {
            logger.log("Received login request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return result;
        }

        if(responsePacket.getOperation().equals("successful")){
            logger.log("Logged in user : " + packet.getPayload(), "Login" );
            result = true;
        } else {
            logger.log("Failed to login user : " + packet.getPayload(), "Error" );
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
        return result;
    }
}