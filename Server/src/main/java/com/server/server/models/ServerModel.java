package com.server.server.models;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerModel class that represents the server.
 * It handles the starting and stopping of the server.
 */
public class ServerModel {
    private static Logger logger; // Logger instance for logging events
    private boolean serverEverStarted = false; // Flag to check if the server has ever been started
    private boolean isOn = false; // Flag to check if the server is currently on
    private ServerSocket serverSocket; // ServerSocket for accepting client connections
    private ExecutorService executorService; // ExecutorService for managing threads
    private ConnectionHandler connectionHandler; // ConnectionHandler for handling client connections
    private int port; // Port number on which the server is running

    /**
     * Getter for the port number.
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Constructor for ServerModel.
     * Initializes the logger.
     */
    public ServerModel() {
        logger = Logger.getInstance();
    }

    /**
     * Method to start the server.
     * If the server has never been started, it sets serverEverStarted to true.
     * If the server is not currently on, it starts the server and logs the event.
     */
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

    /**
     * Method to stop the server.
     * If the server is currently on, it stops the server, closes the serverSocket and executorService, and logs the event.
     */
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

    /**
     * Getter for the isOn flag.
     * @return true if the server is on, false otherwise
     */
    public boolean isOn() {
        return isOn;
    }
}