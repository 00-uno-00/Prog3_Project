package com.client.client.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


//Mandare pacchetti con risposta & refresh periodico
public class CommsHandler {

    private final Socket socket;
    private final ExecutorService executorService;

    private Packet packet;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

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

    public boolean login() throws ExecutionException, InterruptedException {
        Packet loginPacket = new Packet("login", email, email);

        Future<Packet> future = executorService.submit(new PacketHandler(socket, loginPacket));

        if ("successful".equals(future.get().getPayload())) {
            return true;
        } else {//TODO add connection error
            return false;
        }
    }

    public boolean register() throws ExecutionException, InterruptedException {
        Packet registerPacket = new Packet("register", email, email);

        Future<Packet> future = executorService.submit(new PacketHandler(socket, registerPacket));
        if ("successful".equals(future.get().getPayload())) {
            return true;
        } else {//TODO add connection error
            return false;
        }
    }

    //send empty list for first refresh

    public List<Email> refresh(List<Integer> emailIDs) throws ExecutionException, InterruptedException {

            // Create a new "online" packet
            Packet onlinePacket = new Packet("refresh", emailIDs, email);

            Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, onlinePacket));

            // If the response packet's operation is "online", return true
            if ("succesful".equals(responsePacket.get().getOperation())) {
                return (List<Email>) responsePacket.get().getPayload();
            } else {
                return null;//can be handled by model
            }
    }

}
