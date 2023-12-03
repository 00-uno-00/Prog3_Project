package com.client.client.utils.handleStrategies;

import com.client.client.models.Email;
import com.client.client.models.Packet;
import com.client.client.utils.PacketHandlerStrategy;
import com.client.client.utils.PacketUtils;
import com.client.client.utils.specificHandlers.EmailHandler;

import java.io.ObjectOutputStream;

public class EmailPacketStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream) {
        Packet responsePacket;
        if(packet.getPayload() instanceof Email mail){
            EmailHandler emailHandler = new EmailHandler();
            responsePacket = emailHandler.handleEmail(mail, mail.getRecipient());
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
