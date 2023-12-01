package com.server.server.models;

import java.util.concurrent.atomic.AtomicInteger;

public class PacketHandler implements Runnable {
    private final Packet packet;
    private final AtomicInteger id;

    public PacketHandler(Packet packet, AtomicInteger id) {
        this.packet = packet;
        this.id = id;
    }

    @Override
    public void run() { //TODO verify the sender
        Logger logger = Logger.getInstance();
        logger.log("Received packet: " + packet, "Packet");
        switch (packet.getType()) {
            case "mail":
                Email mail = (Email) packet.getContent();
                logger.log("Received mail: " + mail, "Email");
                id.get(); //ecc...
                //TODO handle email
                /*
                 Email handling logic :
                 1) Verify if sender was valid
                 2) Verify if recipient was valid
                      -> If valid: step 3
                      -> If !valid: write Email to sender folder ("recipient not valid. Sincerely, the Server")
                          |-> Send "not sent" Packet to client
                 3) Write Email file into recipient folder
                 4) Send "confirmation" Packet to client
                 */
                break;
            case "login":
                //retrieve user
                logger.log("Received login request from : + user", "Login" );
                //TODO handle login successful or not
                break;
            default:
                logger.log("Received unknown packet type: " + packet.getType(), "Error");
        }
    }
}