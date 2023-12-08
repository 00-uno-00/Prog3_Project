package com.client.client.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class PacketHandler implements Callable<Packet> {


    //private String baseDir = System.getProperty("user.dir");
    //private String relativePath= "/Client/src/main/resources/com/client/client/email/";

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    private Packet packet;

    private Socket socket;

    public PacketHandler(Socket socket, Packet packet) {
        this.socket = socket;
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

    public Packet call() throws IOException {
        System.out.println(packet.toString());
        sendPacket(packet, objectOutputStream);

        int attempts = 0;
        while (attempts < 3) {
            try {
                Thread.sleep(3000);

                Packet responsePacket = getResponse(objectInputStream);

                if (responsePacket != null && "successful".equals(responsePacket.getPayload()) && "failed".equals(responsePacket.getPayload())) {
                    return responsePacket;
                } else {
                    attempts++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
        return new Packet("connectionError", null, "client");
    }

    public static void sendPacket(Packet packet, ObjectOutputStream objectOutputStream) {
        try {
            objectOutputStream.writeObject(packet);
            System.out.println("Packet sent: " + packet.toString());
        } catch (IOException e) {
            System.err.println("Error sending packet to client: " + e.getMessage());
        }
        try {
            objectOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error flushing packet to client: " + e.getMessage());
        }
    }

    public static Packet getResponse(ObjectInputStream objectInputStream) {
        try {
            return (Packet) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error getting response from Server: " + e.getMessage());
            return null;
        }
    }
}