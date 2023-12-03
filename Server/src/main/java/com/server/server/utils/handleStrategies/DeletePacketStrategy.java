package com.server.server.utils.handleStrategies;

import com.server.server.models.Logger;
import com.server.server.models.Packet;
import com.server.server.utils.PacketHandlerStrategy;

import java.io.ObjectOutputStream;

public class DeletePacketStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {

    }
}
