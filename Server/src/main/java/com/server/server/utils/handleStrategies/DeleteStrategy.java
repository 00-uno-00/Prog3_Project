package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.DeleteHandler;

import java.io.ObjectOutputStream;

/**
 * This class implements the PacketHandlerStrategy interface and provides a specific strategy to handle delete packets.
 */
public class DeleteStrategy implements PacketHandlerStrategy {

    /**
     * This method handles the incoming packet and performs the necessary operations based on the packet's payload.
     * If the payload is an integer, it logs the delete request, creates a new DeleteHandler, and calls its delete method.
     * If the payload is not an integer, it logs an error message and returns without doing anything.
     * After handling the packet, it sends a response packet back to the sender.
     *
     * @param packet The incoming packet to be handled.
     * @param objectOutputStream The ObjectOutputStream to send the response packet.
     * @param logger The logger to log the operations.
     */
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