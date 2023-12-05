package com.client.client.utils.handleStrategies;

import com.client.client.models.Packet;
import com.client.client.utils.PacketHandlerStrategy;
import com.client.client.utils.PacketUtils;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketStartegy implements PacketHandlerStrategy {

    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream) {
        //suca
    }

    @Override
    public Boolean handlePacketWithResp(Packet packet, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        Packet sendPacket = new Packet("login", packet.getPayload(), "client");
        PacketUtils.sendPacket(sendPacket, objectOutputStream);

        int attempts = 0;
        while (attempts < 3) {
            try {
                // Wait for 3 seconds for the response
                Thread.sleep(3000);

                // Assuming getResponse is a method that gets the response packet
                Packet responsePacket = PacketUtils.getResponse(objectInputStream);

                if (responsePacket != null && "successful".equals(responsePacket.getPayload())) {
                    // If the response is successful, break the loop
                    break;
                } else {
                    // If the response is not successful, increment the attempts counter
                    attempts++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return attempts < 3;
    }
}