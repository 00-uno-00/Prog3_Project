package com.client.client.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


//Mandare pacchetti con risposta & refresh periodico
public class CommsHandler {

    private final Socket socket;
    private final ExecutorService executorService;

    private Packet packet;

    private String email;

    private volatile boolean connected;
    /**
     * Handles the connection to the server
     * @param socket
     * @param executorService
     */
    public CommsHandler(Socket socket, ExecutorService executorService, String email) {
        this.socket = socket;
        this.executorService = executorService;
        this.email = email;
    }

    public boolean login(){
        Packet loginPacket = new Packet("login", new Contact(email), email);

        Future<Packet> future = executorService.submit(new PacketHandler(socket, loginPacket, email));

        return true;
    }

    //send empty list for first refresh
    /*
    public void refresh(String email, List<Integer> emailIDs) {
        try {
            // Create a new "online" packet
            Packet onlinePacket = new Packet("refresh", emailIDs, email);

            Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, onlinePacket));

            // If the response packet's operation is "online", return true
            if ("succesful".equals(responsePacket.get().getOperation())) {//TODO FIX

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // If the response packet's operation is not "online", return false
        connected = false;
    }
    */
}
