package com.server.server.utils;

import com.server.server.models.Packet;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PacketUtils {

    public static boolean isValidSender(String sender) {
        if (sender == null || sender.isEmpty()) {
            return false;
        }
        // check if the sender String is equal to
        // the name of a folder in the directory:
        //  "Server/src/main/resources/com/server/server/accounts"
        return new File("Server/src/main/resources/com/server/server/accounts/" + sender).exists() || sender.equals("client");
    }

    public static void sendPacket(Packet packet, ObjectOutputStream objectOutputStream) {
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

}
