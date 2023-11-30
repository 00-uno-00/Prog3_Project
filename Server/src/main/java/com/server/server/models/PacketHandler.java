package com.server.server.models;

public class PacketHandler implements Runnable {
    private final Packet packet;

    public PacketHandler(Packet packet) {
        this.packet = packet;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        logger.log("Received packet: " + packet, "Packet");
        switch (packet.getType()) {
            case "mail":
                Email mail = packet.getEmail();
                logger.log("Received mail: " + mail, "Email");
                //TODO handle email
                break;
            case "user":
                //retrieve user
                logger.log("Received login request from : + user", "Login" );
                //TODO handle login successful or not
                break;
            default:
                logger.log("Received unknown packet type: " + packet.getType(), "Error");
        }
    }
}