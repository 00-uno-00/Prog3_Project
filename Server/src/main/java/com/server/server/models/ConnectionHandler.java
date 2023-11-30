package com.server.server.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ConnectionHandler implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private volatile boolean running;

    public ConnectionHandler(ServerSocket serverSocket, ExecutorService executorService) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.running = true;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Packet packet = (Packet) objectInputStream.readObject();
                executorService.submit(new PacketHandler(packet));
            } catch (IOException e) {
                if (running) { // Only log the error if the server is supposed to be running
                    logger.log("Error with network connection: " + e.getMessage(), "Error");
                }
            } catch (ClassNotFoundException e) {
                logger.log("Error reading packet: " + e.getMessage(), "Error");
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}