package com.server.server.utils.handleStrategies;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.specificHandlers.EmailHandler;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.models.Logger;

import java.io.ObjectOutputStream;

public class EmailStrategy implements PacketHandlerStrategy {
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        if(packet.getPayload() instanceof Email mail){
            logger.log("Received email from : " + mail.getSender(), "Email" );
            EmailHandler emailHandler = new EmailHandler();
            responsePacket = emailHandler.email(mail, mail.getRecipient());
        } else {
            logger.log("Received email request with invalid Payload type : " + packet.getPayload().getClass(), "Error" );
            return;
        }

        if(responsePacket.getOperation().equals("successful")){
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
            logger.log("Sent email from : " + packet.getPayload(), "Email" );
        } else {
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
            logger.log("Failed to send email from : " + packet.getPayload(), "Error" );
        }
    }
}
