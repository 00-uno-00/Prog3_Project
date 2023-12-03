package com.client.client.utils;

import com.client.client.models.Packet;


import java.io.ObjectOutputStream;

public interface PacketHandlerStrategy {
    void handlePacket(Packet packet, ObjectOutputStream objectOutputStream);
}
