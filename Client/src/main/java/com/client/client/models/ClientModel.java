package com.client.client.models;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientModel {
    private Socket clientSocket;
    private int port;

    private ExecutorService executorService;

    private PacketHandler packetHandler;

    private ConnectionHandler connectionHandler;

    public int getPort() {
        return port;
    }

    public ClientModel() {
            try {
                clientSocket = new Socket("localhost",8081);
                port = clientSocket.getLocalPort();
                executorService = Executors.newFixedThreadPool(2);
                connectionHandler = new ConnectionHandler(clientSocket, executorService);
                new Thread(connectionHandler).start();
            } catch (Exception e) {

            }

    }

    public boolean login(String email){
        //newpacket login
        Packet packet = new Packet("login", new Contact(email), "client");
        connectionHandler.setPacket(packet);
        if () {
            return true;
        }
        return false;
    }

}
