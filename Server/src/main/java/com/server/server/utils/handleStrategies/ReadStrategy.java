package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.utils.specificHandlers.ReadHandler;

import java.io.ObjectOutputStream;

/**
 * Strategy to handle read requests
 * Implements PacketHandlerStrategy
 */
public class ReadStrategy implements PacketHandlerStrategy {

    /**
     * Handles read requests
     * @param packet Read Packet to be handled
     * @param objectOutputStream ObjectOutputStream to write response to
     * @param logger Logger to log to
     * @return boolean true if successful, false otherwise
     */
    @Override
    public boolean handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        boolean result = false;
        if(packet.getPayload() instanceof Integer id){
            logger.log("Received read request from : " + packet.getSender(), "Read" );
            ReadHandler readHandler = new ReadHandler();
            responsePacket = readHandler.read(id, packet.getSender());
        } else {
            logger.log("Received read request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return result;
        }
        if(responsePacket.getOperation().equals("successful")){
            logger.log("Read email : " + id + " of user : " + packet.getSender(), "Read" );
            result = true;
        } else {
            logger.log("Failed to read email : " + id + " of user : " + packet.getSender(), "Error" );
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
        return result;
    }
}