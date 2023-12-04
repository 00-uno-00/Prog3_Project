package com.client.client.utils;

import com.client.client.models.Packet;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface PacketHandlerStrategy {
    void handlePacket(Packet packet, ObjectOutputStream objectOutputStream);

    Boolean handlePacketWithResp(Packet packet, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream);
}
