package com.client.client.models;

import com.client.client.controllers.ClientController;
import com.client.client.utils.PacketHandlerStrategy;
import com.client.client.utils.PacketUtils;
import com.client.client.utils.handleStrategies.DeletePacketStrategy;
import com.client.client.utils.handleStrategies.EmailPacketStrategy;
import com.client.client.utils.handleStrategies.LoginPacketStrategy;
import com.client.client.utils.handleStrategies.RegisterPacketStrategy;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class PacketHandler implements Runnable {


    private String baseDir = System.getProperty("user.dir");
    private String relativePath= "/Client/src/main/resources/com/client/client/email/";

    private final Packet packet;

    private final Map<String, PacketHandlerStrategy> strategies;
    /**
     * Handles the packet and generates a response packet
     * @param packet
     * @param objectOutputStream
     */
    public PacketHandler(Packet packet, ObjectOutputStream objectOutputStream) {
        this.strategies = new HashMap<>();
        this.strategies.put("register", new RegisterPacketStrategy());
        this.strategies.put("login", new LoginPacketStrategy());
        this.strategies.put("mail", new EmailPacketStrategy());
        this.strategies.put("delete", new DeletePacketStrategy());
        this.packet = packet;
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