package com.client.client.utils.handleStrategies;

import com.client.client.models.Packet;
import com.client.client.utils.PacketHandlerStrategy;
import com.client.client.utils.PacketUtils;
import com.client.client.utils.specificHandlers.RegisterHandler;

import java.io.ObjectOutputStream;

public class RegisterPacketStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream) {
        Packet responsePacket;
        if(packet.getPayload() instanceof String username){
            RegisterHandler registerHandler = new RegisterHandler();
            responsePacket = registerHandler.register(username);
        } else {
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
        } else {
        }
        PacketUtils.sendPacket(responsePacket, objectOutputStream);
    }
}
