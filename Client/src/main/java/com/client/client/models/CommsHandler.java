package com.client.client.models;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.server.server.models.Email;
import com.server.server.models.Packet;

//Mandare pacchetti con risposta & refresh periodico
public class CommsHandler {

    private Socket socket;
    private final ExecutorService executorService;

    private Packet packet;

    private String sender;

    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Handles the connection to the server
     * 
     * @param executorService
     */
    public CommsHandler(ExecutorService executorService, String sender) throws IOException {
        this.executorService = executorService;
        this.sender = sender;
    }

    public String login() throws ExecutionException, InterruptedException, IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        Packet loginPacket = new Packet("login", sender, "client");

        Future<Packet> future = executorService.submit(new PacketHandler(socket, loginPacket));

        if ("successful".equals(future.get().getOperation())) {
            return "successful";
        } else {
            return future.get().getPayload().toString();
        }
    }

    public String register() throws ExecutionException, InterruptedException, IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        Packet registerPacket = new Packet("register", sender, "client");

        Future<Packet> future = executorService.submit(new PacketHandler(socket, registerPacket));
        if ("successful".equals(future.get().getOperation())) {
            return "successful";
        } else {
            return future.get().getPayload().toString();
        }
    }

    // send empty list for first refresh

    public List<Email> refresh(List<Integer> emailIDs) throws ExecutionException, InterruptedException, IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        Packet onlinePacket = new Packet("refresh", emailIDs, sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, onlinePacket));

        // If the response packet's operation is "online", return true
        if ("succesful".equals(responsePacket.get().getOperation())) {
            return (List<Email>) responsePacket.get().getPayload();
        } else {
            return null;// can be handled by model
        }
    }

    public String send(Email email) throws ExecutionException, InterruptedException, UnknownHostException, IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);

        Packet sendPacket = new Packet("email", email, this.sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, sendPacket));

        if ("successful".equals(responsePacket.get().getOperation())) {
            return "successful";
        } else {
            return responsePacket.get().getPayload().toString();
        }
    }

    public boolean delete(List<Integer> emailIDs) throws ExecutionException, InterruptedException {
        Packet deletePacket = new Packet("delete", emailIDs, sender);

        Future<Packet> responsePacket = executorService.submit(new PacketHandler(socket, deletePacket));

        return "successful".equals(responsePacket.get().getPayload());
    }

}
