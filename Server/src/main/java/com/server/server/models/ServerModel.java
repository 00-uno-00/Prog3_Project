package com.server.server.models;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerModel {
    private static Logger logger;
    private boolean serverEverStarted = false;
    private boolean isOn = false;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private ConnectionHandler connectionHandler;
    private int port;

    public int getPort() {
        return port;
    }

    public ServerModel() {
        logger = Logger.getInstance();
    }

    public void startServer() {
        if(!serverEverStarted)
            serverEverStarted = true;

        if(!isOn) {
            isOn = true;
            logger.log("Started Server" , "System");

            try {
                serverSocket = new ServerSocket(8081);
                port = serverSocket.getLocalPort();
                executorService = Executors.newFixedThreadPool(10);
                connectionHandler = new ConnectionHandler(serverSocket, executorService, serverEverStarted);
                new Thread(connectionHandler).start();
            } catch (IOException e) {
                logger.log("An error occurred while trying to start the server", "Error");
            }
        }
    }

    public void stopServer() {
        if(isOn){
            connectionHandler.stop();
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (executorService != null) {
                    executorService.shutdown();
                }
            } catch (IOException e) {
                logger.log("An error occurred while trying to stop the server", "Error");
            }
            isOn = false;
            logger.log("Stopped Server ", "System");
        }
    }

    public boolean isOn() {
        return isOn;
    }
}