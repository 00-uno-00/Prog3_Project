package com.server.server.models;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionHandler implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private volatile boolean running;
    private final boolean serverEverStarted;
    private AtomicInteger id;
    private final File idFile;

    public ConnectionHandler(ServerSocket serverSocket, ExecutorService executorService, boolean serverEverStarted) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.running = true;
        this.serverEverStarted = serverEverStarted;
        String baseDir = System.getProperty("user.dir");
        String relativePath = "/Server/src/main/resources/com/server/server/id/";
        this.idFile = new File(baseDir + relativePath, "id.txt");
        this.id = retrieveID();
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                executorService.submit(new PacketHandler(id, socket));
            } catch (IOException e) {
                if (running) {
                    logger.log("Error with network connection: " + e.getMessage(), "Error");
                }
            }
        }
        storeId();
    }

    public void stop() {
        this.running = false;
    }

    private synchronized AtomicInteger retrieveID() {
        Logger logger = Logger.getInstance();
        try {
            if (!idFile.exists() && idFile.createNewFile()) {
                logger.log("Created Mail ID file (default: 0) ", "System");
            }
            Scanner scanner = new Scanner(idFile);
            id = new AtomicInteger(scanner.hasNextInt() ? scanner.nextInt() : 0);
            logger.log("Retrieved Mail ID from file (" + id.get() + ") ", "System");
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException("Error handling id file", e);
        }
        return id;
    }

    private synchronized void storeId() {
        if (!serverEverStarted) {
            Logger logger = Logger.getInstance();
            logger.log("Closing Application, won't store ID since Server never started ", "System");
            return;
        }
        try (PrintWriter writer = new PrintWriter(idFile)) {
            writer.println(id);
            writer.flush();
            writer.close();
            Logger logger = Logger.getInstance();
            logger.log("Stored Mail ID to file (" + id.get() + ") ", "System");
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to id file");
        }
    }
}