package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.RefreshHandler;

import java.io.ObjectOutputStream;
import java.util.List;

public class RefreshStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        String username = packet.getSender();
        if(packet.getPayload() instanceof List){
            logger.log("Received refresh request from : " + username, "Refresh" );
            RefreshHandler refreshHandler = new RefreshHandler();
            responsePacket = refreshHandler.refresh(username, (List<Integer>) packet.getPayload());
            //TODO try to make this cast safer
        } else {
            logger.log("Received refresh request with invalid Payload type : " + packet.getPayload().getClass(), "Refresh" );
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
            logger.log("Successfully sent emails to : " + username, "Refresh" );
        } else {
            logger.log("Failed to send emails to : " + username, "Error" );
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
    }
}
