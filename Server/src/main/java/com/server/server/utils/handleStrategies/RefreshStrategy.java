package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.RefreshHandler;

import java.io.ObjectOutputStream;
import java.util.List;

/**
 * This class implements the PacketHandlerStrategy interface and provides a specific strategy to handle refresh packets.
 */
public class RefreshStrategy implements PacketHandlerStrategy {

    /**
     * This method handles the incoming packet and performs the necessary operations based on the packet's payload.
     * If the payload is a list, it logs the refresh request, creates a new RefreshHandler, and calls its refresh method.
     * If the payload is not a list, it logs an error message and returns without doing anything.
     * After handling the packet, it sends a response packet back to the sender.
     *
     * @param packet The incoming packet to be handled.
     * @param objectOutputStream The ObjectOutputStream to send the response packet.
     * @param logger The logger to log the operations.
     */
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        String username = packet.getSender();
        if(packet.getPayload() instanceof List){
            logger.log("Received refresh request from : " + username, "Refresh" );
            RefreshHandler refreshHandler = new RefreshHandler();
            responsePacket = refreshHandler.refresh(username, (List<Integer>) packet.getPayload());
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