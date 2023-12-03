package com.server.server.models;

import com.server.server.utils.*;
import com.server.server.utils.handleStrategies.DeletePacketStrategy;
import com.server.server.utils.handleStrategies.RegisterPacketStrategy;
import com.server.server.utils.handleStrategies.LoginPacketStrategy;
import com.server.server.utils.handleStrategies.EmailPacketStrategy;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketHandler implements Runnable {
    private final Packet packet;
    private final AtomicInteger id;
    private final ObjectOutputStream objectOutputStream;
    private final Map<String, PacketHandlerStrategy> strategies;

    public PacketHandler(Packet packet, AtomicInteger id, ObjectOutputStream objectOutputStream) {
        this.packet = packet;
        this.id = id;
        this.objectOutputStream = objectOutputStream;
        this.strategies = new HashMap<>();
        this.strategies.put("register", new RegisterPacketStrategy());
        this.strategies.put("login", new LoginPacketStrategy());
        this.strategies.put("mail", new EmailPacketStrategy());
        this.strategies.put("delete", new DeletePacketStrategy());
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        if(!PacketUtils.isValidSender(packet.getSender())){
            logger.log("Received packet with invalid sender: " + packet.getSender(), "Error");
            Packet responsePacket = new Packet("failed", "invalid sender", "server");
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
            return;
        }

        PacketHandlerStrategy strategy = strategies.get(packet.getOperation());
        if (strategy != null) {
            if(strategy instanceof EmailPacketStrategy){ //introduce id to email
                Email email = (Email) packet.getPayload(); //check if payload is email
                email.setId(id.getAndIncrement());
                packet.setPayload(email);
            }
            strategy.handlePacket(packet, objectOutputStream, logger);
        } else {
            Packet responsePacket = new Packet("failed", "unknown packet operation", "server");
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
            logger.log("Received unknown packet type: " + packet.getOperation(), "Error");
        }
    }
}