package com.client.client.utils.handleStrategies;

import com.client.client.models.Packet;
import com.client.client.utils.PacketHandlerStrategy;
import com.client.client.utils.PacketUtils;
import com.client.client.utils.specificHandlers.LoginHandler;

import java.io.ObjectOutputStream;

public class LoginPacketStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream ) {
        Packet responsePacket;
        if(packet.getPayload() instanceof String username){
            LoginHandler loginHandler = new LoginHandler();
            responsePacket = loginHandler.login(username);
        } else {
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
        } else {
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
        }
    }
}