package com.server.server.models;

import com.server.server.utils.*;
import com.server.server.utils.handleStrategies.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketHandler implements Runnable {
    private Packet packet;
    private final AtomicInteger id;
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final Map<String, PacketHandlerStrategy> strategies;

    public PacketHandler(AtomicInteger id, Socket socket) {
        this.id = id;
        this.socket = socket;
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error creating output stream: " + e.getMessage());
        }
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error creating input stream: " + e.getMessage());
        }
        try {
            this.packet = (Packet) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading packet: " + e.getMessage());
        }
        this.strategies = new HashMap<>();
        this.strategies.put("register", new RegisterStrategy());
        this.strategies.put("login", new LoginStrategy());
        this.strategies.put("mail", new EmailStrategy());
        this.strategies.put("delete", new DeleteStrategy());
        this.strategies.put("refresh", new RefreshStrategy());
    }

    private void closeConnections() {
        if (socket != null) {
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
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
            if(strategy instanceof EmailStrategy){ //introduce id to email
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
        closeConnections();
    }
}