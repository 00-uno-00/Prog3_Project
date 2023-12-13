package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.RegisterHandler;

import java.io.ObjectOutputStream;

/**
 * Strategy for handling register requests.
 * Implements the PacketHandlerStrategy interface.
 */
public class RegisterStrategy implements PacketHandlerStrategy {

    /**
     * Method to handle a register request packet.
     * If the payload of the packet is a String, it logs the request and calls the register method of RegisterHandler.
     * If the payload is not a String, it logs an error and returns.
     * After handling the packet, it sends a response packet indicating the result of the operation.
     * @param packet The packet to handle
     * @param objectOutputStream The ObjectOutputStream to write the response to
     * @param logger The logger to log events
     */
    @Override
    public boolean handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        boolean result = false;
        if(packet.getPayload() instanceof String username){
            logger.log("Received register request from : " + username, "Register" );
            RegisterHandler registerHandler = new RegisterHandler();
            responsePacket = registerHandler.register(username);
        } else {
            logger.log("Received register request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return result;
        }

        if(responsePacket.getOperation().equals("successful")){
            logger.log("Registered user : " + packet.getPayload(), "Register" );
            result = true;
        } else {
            logger.log("Failed to register user : " + packet.getPayload(), "Error" );
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
        return result;
    }
}