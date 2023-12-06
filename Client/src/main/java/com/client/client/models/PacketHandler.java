package com.client.client.models;

import com.client.client.PacketUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class PacketHandler implements Callable<Packet> {


    //private String baseDir = System.getProperty("user.dir");
    //private String relativePath= "/Client/src/main/resources/com/client/client/email/";

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    private Packet packet;

    public PacketHandler(Socket socket, Packet packet) {
        this.packet = packet;
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
    }

    public Packet call() throws Exception {
        Packet sendPacket = new Packet(packet.getOperation(), packet.getPayload(), packet.getSender());
        PacketUtils.sendPacket(sendPacket, objectOutputStream);

        int attempts = 0;
        while (attempts < 3) {
            try {
                Thread.sleep(3000);

                Packet responsePacket = PacketUtils.getResponse(objectInputStream);

                if (responsePacket != null && "successful".equals(responsePacket.getPayload()) && "failed".equals(responsePacket.getPayload())) {
                    return responsePacket;
                } else {
                    attempts++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new Packet("connectionError", null, "client");
    }
}