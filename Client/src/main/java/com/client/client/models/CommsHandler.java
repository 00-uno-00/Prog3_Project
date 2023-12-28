package com.client.client.models;

import java.io.IOException;
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

    private Packet packet;

    private String sender;

    /**
     * Sets the sender email address for the communication handler.
     * @param sender The sender's email address.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Constructor for the CommsHandler class.
     * @param executorService The ExecutorService for handling asynchronous tasks.
     * @param sender The sender's email address.
     */
    public CommsHandler(ExecutorService executorService, String sender) {
        this.executorService = executorService;
        this.sender = sender;
    }

    /**
     * Attempts to log in to the server.
     * @return A string indicating the result of the login attempt.
     * @throws ExecutionException If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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

    /**
     * Attempts to register the client with the server.
     * @return A string indicating the result of the registration attempt.
     * @throws ExecutionException If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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

    /**
     * Refreshes the email list based on the provided email IDs.
     * @param emailIDs The list of email IDs to refresh.
     * @return A list of refreshed emails.
     * @throws ExecutionException If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public List<Email> refresh(List<Integer> emailIDs) throws ExecutionException, InterruptedException{
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8081);
        } catch (Exception e) {
            Email email = new Email();
            email.setSender("Server Offline");
            List<Email> emailList = Collections.singletonList(email); // return a list with one email with the sender "Server Offline"
            return emailList;
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

    /**
     * Sends an email.
     * @param email The Email object to be sent.
     * @return A string indicating the result of the send operation.
     * @throws ExecutionException If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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

    /**
     * Deletes an email based on its ID.
     * @param ID The ID of the email to be deleted.
     * @return A string indicating the result of the delete operation.
     * @throws ExecutionException If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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

    /**
     * Marks an email as read based on its ID.
     * @param id The ID of the email to be marked as read.
     * @return A string indicating the result of the read operation.
     * @throws ExecutionException If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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
