package com.client.client.models;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import com.server.server.models.Email;
import com.server.server.models.Packet;

public class CommsHandler {

    private Socket socket;
    private final ExecutorService executorService;
    private String sender;

    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Handles the connection to the server
     *
     * @param executorService the executor service to handle the threads
     */
    public CommsHandler(ExecutorService executorService, String sender) {
        this.executorService = executorService;
        this.sender = sender;
    }

    public String login() throws ExecutionException, InterruptedException {
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            return "connection error";
        }
        
        Packet loginPacket = new Packet("login", sender, "client");

        Future<Packet> future = executorService.submit(new PacketHandler(socket, loginPacket));

        if ("successful".equals(future.get().getOperation())) {
            return "successful";
        } else {
            return future.get().getPayload().toString();
        }
    }

    public String register() throws ExecutionException, InterruptedException {
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            return "connection error";
        }
        
        Packet registerPacket = new Packet("register", sender, "client");

        Future<Packet> future = executorService.submit(new PacketHandler(socket, registerPacket));
        if ("successful".equals(future.get().getOperation())) {
            return "successful";
        } else {
            return future.get().getPayload().toString();
        }
    }

    // send empty list for first refresh

    public List<Email> refresh(List<Integer> emailIDs) throws ExecutionException, InterruptedException{
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            Email email = new Email();
            email.setSender("Server Offline");
            return Collections.singletonList(email);
        }
        
        Packet onlinePacket = new Packet("refresh", emailIDs, sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, onlinePacket));

        // If the response packet's operation is "online", return true
        if ("successful".equals(responsePacket.get().getOperation())) {
            return (List<Email>) responsePacket.get().getPayload();
        } else {
            return null;// can be handled by model
        }
    }

    public String send(Email email) throws ExecutionException, InterruptedException {
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            return "connection error";
        }
        

        Packet sendPacket = new Packet("email", email, this.sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, sendPacket));

        if ("successful".equals(responsePacket.get().getOperation())) {
            return "successful";
        } else {
            return responsePacket.get().getPayload().toString();
        }
    }

    public String delete(Integer ID) throws ExecutionException, InterruptedException {
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            return "connection error";
        }

        Packet deletePacket = new Packet("delete", ID, sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, deletePacket));

        if ("successful".equals(responsePacket.get().getOperation())) {
            return "successful";
        } else {
            return responsePacket.get().getPayload().toString();
        }
    }

    public String read(Integer id) throws ExecutionException, InterruptedException {
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            return "connection error";
        }

        Packet readPacket = new Packet("read", id, sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, readPacket));

        if ("successful".equals(responsePacket.get().getOperation())) {
            return "successful";
        } else {
            return responsePacket.get().getPayload().toString();
        }
    }
}
