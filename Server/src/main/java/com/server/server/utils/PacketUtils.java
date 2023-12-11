package com.server.server.utils;

import com.server.server.models.Packet;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Utility class for handling packets.
 */
public class PacketUtils {

    /**
     * Method to validate the sender of a packet.
     * Checks if the sender is not null or empty, and if it corresponds to an existing account.
     * @param sender The sender to validate
     * @return true if the sender is valid, false otherwise
     */
    public static boolean isValidSender(String sender) {
        if (sender == null || sender.isEmpty()) {
            return false;
        }
        // check if the sender String is equal to
        // the name of a folder in the directory:
        //  "Server/src/main/resources/com/server/server/accounts"
        return new File("Server/src/main/resources/com/server/server/accounts/" + sender).exists() || sender.equals("client");
    }

    /**
     * Method to send a packet to a client.
     * Writes the packet to the ObjectOutputStream and flushes the stream.
     * @param packet The packet to send
     * @param objectOutputStream The ObjectOutputStream to write the packet to
     */
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