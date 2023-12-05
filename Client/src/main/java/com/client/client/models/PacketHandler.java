package com.client.client.models;

import com.client.client.utils.PacketHandlerStrategy;
import com.client.client.utils.PacketUtils;
import com.client.client.utils.handleStrategies.DeletePacketStrategy;
import com.client.client.utils.handleStrategies.EmailPacketStrategy;
import com.client.client.utils.handleStrategies.PacketStartegy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler implements Runnable {


    private String baseDir = System.getProperty("user.dir");
    private String relativePath= "/Client/src/main/resources/com/client/client/email/";

    private final ObjectOutputStream objectOutputStream;

    private final ObjectInputStream objectInputStream;

    private Packet packet;

    private final Map<String, PacketHandlerStrategy> strategies;

    public PacketHandler(Socket socket, Packet packet) {
        this.strategies = new HashMap<>();
        this.strategies.put("login", new PacketStartegy());
        this.strategies.put("mail", new EmailPacketStrategy());
        this.strategies.put("delete", new DeletePacketStrategy());
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            this.packet = (Packet) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        PacketHandlerStrategy strategy = strategies.get(packet.getOperation());
        if (strategy != null) {
            if(strategy instanceof EmailPacketStrategy){ //introduce id to email
                Email email = (Email) packet.getPayload(); //check if payload is email
                packet.setPayload(email);
            }
            strategy.handlePacket(packet, objectOutputStream);
        } else {
            Packet responsePacket = new Packet("failed", "unknown packet operation", "client");
            PacketUtils.sendPacket(responsePacket, objectOutputStream);
        }
        /*
            switch (packet.getType()) {
                case "mail"://TODO move to specific folder
                    if (packet.getContent() instanceof ArrayList) {
                        for (Object email: (ArrayList<?>) packet.getContent()) {
                            if (email instanceof Email) {
                                controller.handleEmail((Email) email);
                                //TODO handle email
                            } else {
                                break;
                            }

                        }
                    }
                    //TODO handle email
                    break;
                case "user":

                    break;
                default:
            }*/

    }
}