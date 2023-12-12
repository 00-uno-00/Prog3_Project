package com.server.server.utils.handleStrategies;

import com.server.server.models.Email;
import com.server.server.models.Packet;
import com.server.server.utils.specificHandlers.EmailHandler;
import com.server.server.utils.PacketHandlerStrategy;
import com.server.server.utils.PacketUtils;
import com.server.server.models.Logger;

import java.io.ObjectOutputStream;

/**
 * This class implements the PacketHandlerStrategy interface and provides a specific strategy to handle email packets.
 */
public class EmailStrategy implements PacketHandlerStrategy {

    /**
     * This method handles the incoming packet and performs the necessary operations based on the packet's payload.
     * If the payload is an instance of Email, it logs the email request, creates a new EmailHandler, and calls its email method.
     * If the payload is not an instance of Email, it logs an error message and returns without doing anything.
     * After handling the packet, it sends a response packet back to the sender.
     *
     * @param packet The incoming packet to be handled.
     * @param objectOutputStream The ObjectOutputStream to send the response packet.
     * @param logger The logger to log the operations.
     */
    @Override
    public void handlePacket(Packet packet, ObjectOutputStream objectOutputStream, Logger logger) {
        Packet responsePacket;
        if(packet.getPayload() instanceof Email mail){
            logger.log("Received email from : " + mail.getSender(), "Email" );
            EmailHandler emailHandler = new EmailHandler();
            responsePacket = emailHandler.email(mail, mail.getRecipients());
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