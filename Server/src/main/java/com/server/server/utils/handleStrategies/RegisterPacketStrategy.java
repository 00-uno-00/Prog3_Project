package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.RegisterHandler;

import java.io.ObjectOutputStream;

public class RegisterPacketStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        if(packet.getPayload() instanceof String username){
            logger.log("Received register request from : " + username, "Register" );
            RegisterHandler registerHandler = new RegisterHandler();
            responsePacket = registerHandler.register(username);
        } else {
            logger.log("Received register request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
            logger.log("Registered user : " + packet.getPayload(), "Register" );
        } else {
            logger.log("Failed to register user : " + packet.getPayload(), "Error" );
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
    }
}
