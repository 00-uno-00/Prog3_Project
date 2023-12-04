package com.client.client.models;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final ExecutorService executorService;

    private volatile boolean connected;
    /**
     * Handles the connection to the server
     * @param socket
     * @param executorService
     */
    public ConnectionHandler(Socket socket, ExecutorService executorService) {
        this.socket = socket;
        this.executorService = executorService;
        this.connected = true;
    }

    @Override
    public void run() {
        while (connected) {// must ping server to keep connection alive
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                Packet packet = (Packet) objectInputStream.readObject();
                executorService.submit(new PacketHandler(packet, objectOutputStream));
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
            }
        }
    }

    public void verifyConnection() {//necessario?
        try {
            // Create a new "online" packet
            Packet onlinePacket = new Packet("online", null,"client");

            // Send the "online" packet to the server
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(onlinePacket);
            objectOutputStream.flush();

            // Wait for a response from the server
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Packet responsePacket = (Packet) objectInputStream.readObject();

            // If the response packet's operation is "online", return true
            if ("online".equals(responsePacket.getOperation())) {
                connected = true;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // If the response packet's operation is not "online", return false
        connected = false;
    }
}
