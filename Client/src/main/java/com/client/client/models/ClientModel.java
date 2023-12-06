package com.client.client.models;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientModel {
    private Socket clientSocket;
    private int port;

    private ExecutorService executorService;

    private PacketHandler packetHandler;

    private CommsHandler commsHandler;

    private String email;

    public int getPort() {
        return port;
    }

    public ClientModel() {
            try {
                clientSocket = new Socket(InetAddress.getLocalHost().getHostName(),8081);
                port = clientSocket.getLocalPort();
                executorService = Executors.newFixedThreadPool(2);
                commsHandler = new CommsHandler(clientSocket, executorService, email);
                System.out.println("Client model started");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    public boolean login(String email){
        //newpacket login
        Packet packet = new Packet("login", new Contact(email), email);
        //lancio thread per inviare pacchetto e mi aspetto risposta
        return commsHandler.login();
    }

    public void setEmail(String email){
        this.email = email;
    }
}
