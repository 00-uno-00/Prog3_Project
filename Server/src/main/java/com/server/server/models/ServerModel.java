package com.server.server.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerModel {
    private static File idFile;
    private static AtomicInteger id;
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
        String baseDir = System.getProperty("user.dir");
        String relativePath = "/Server/src/main/resources/com/server/server/id/";
        idFile = new File(baseDir + relativePath, "id.txt");
        id = new AtomicInteger(0);
        logger = Logger.getInstance();
    }

    public synchronized AtomicInteger retrieveID() {
        try {
            if (!idFile.exists() && idFile.createNewFile()) {
                logger.log("Created Mail ID file (default: 0) ", "System");
            }
            Scanner scanner = new Scanner(idFile);
            id.set(scanner.hasNextInt() ? scanner.nextInt() : 0);
            logger.log("Retrieved Mail ID from file (" + id.get() + ") ", "System");
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException("Error handling id file", e);
        }
        return id;
    }

    public synchronized void storeId() {
        if (!serverEverStarted) {
            logger.log("Closing Application, won't store ID since Server never started ", "System");
            return;
        }
        try (PrintWriter writer = new PrintWriter(idFile)) {
            writer.println(id);
            logger.log("Stored Mail ID to file (" + id.get() + ") ", "System");
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to id file");
        }
    }

    public void startServer() {
        if(!serverEverStarted)
            serverEverStarted = true;

        if(!isOn) {
            isOn = true;
            logger.log("Started Server" , "System");
            id = retrieveID();

            try {
                serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();
                executorService = Executors.newFixedThreadPool(10);
                connectionHandler = new ConnectionHandler(serverSocket, executorService);
                new Thread(connectionHandler).start();
            } catch (IOException e) {
                logger.log("An error occurred while trying to start the server", "Error");
            }
        }
    }

    public void stopServer() {
        if(isOn){
            connectionHandler.stop();
            storeId();
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