package com.server.server.utils;

import com.server.server.models.Logger;
import com.server.server.models.Packet;

import java.io.ObjectOutputStream;

public interface PacketHandlerStrategy {
    void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger);
}
