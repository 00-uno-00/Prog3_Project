package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.DeleteHandler;

import java.io.ObjectOutputStream;

public class DeleteStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        if(packet.getPayload() instanceof Integer id){
            logger.log("Received delete request from : " + packet.getSender(), "Delete" );
            DeleteHandler deleteHandler = new DeleteHandler();
            responsePacket = deleteHandler.delete(id, packet.getSender());
        } else {
            logger.log("Received delete request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
            logger.log("Deleted email : " + id + " of user : " + packet.getSender(), "Delete" );
        } else {
            logger.log("Failed to delete email : " + id + " of user : " + packet.getSender(), "Error" );
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
    }
}
