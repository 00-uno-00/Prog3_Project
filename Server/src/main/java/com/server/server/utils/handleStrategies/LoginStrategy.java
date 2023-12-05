package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.specificHandlers.LoginHandler;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;

import java.io.ObjectOutputStream;

public class LoginStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        if(packet.getPayload() instanceof String username){
            logger.log("Received login request from : " + username, "Login" );
            LoginHandler loginHandler = new LoginHandler();
            responsePacket = loginHandler.login(username);
        } else {
            logger.log("Received login request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
            logger.log("Logged in user : " + packet.getPayload(), "Login" );
        } else {
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
            logger.log("Failed to login user : " + packet.getPayload(), "Error" );
        }
    }
}