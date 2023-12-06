package com.client.client;

import com.client.client.models.Packet;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketUtils {

    public static boolean isValidSender(String sender) {
        if (sender == null || sender.isEmpty()) {
            return false;
        }
        // check if the sender String is equal to
        // the name of a folder in the directory:
        //  "Server/src/main/resources/com/server/server/accounts"
        return new File("Server/src/main/resources/com/server/server/accounts/" + sender).exists();
    }

    public static void sendPacket(Packet packet, ObjectOutputStream objectOutputStream) {//TODO send/recieve
        try {
            objectOutputStream.writeObject(packet);
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
            System.err.println("Error getting response from client: " + e.getMessage());
            return null;
        }
    }
}