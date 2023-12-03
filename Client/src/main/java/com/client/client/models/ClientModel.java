package com.client.client.models;

import java.net.Socket;

public class ClientModel {
    private Socket clientSocket;
    private int port;

    private PacketHandler packetHandler;

    public int getPort() {
        return port;
    }

    public ClientModel() {
            try {
                clientSocket = new Socket("localhost",8081);
                port = clientSocket.getLocalPort();
                Thread thread = new Thread();
                packetHandler = new PacketHandler(clientSocket, thread);
            } catch (Exception e) {

            }

    }

}
