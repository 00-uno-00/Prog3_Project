package com.server.server.utils;

import com.server.server.models.Logger;
import com.server.server.models.Packet;

import java.io.ObjectOutputStream;

/**
 * Interface for handling packets.
 * This interface defines a method for handling packets received from clients.
 */
public interface PacketHandlerStrategy {

    /**
     * Method to handle a packet.
     * This method should be implemented to handle a packet in a specific way.
     * @param packet The packet to handle
     * @param objectOutputStream The ObjectOutputStream to write the response to
     * @param logger The logger to log events
     */
    boolean handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger);
}