package com.server.server.models;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is responsible for handling connections to the server.
 * It implements Runnable to be able to run in a separate thread.
 */
public class ConnectionHandler implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private volatile boolean running;
    private final boolean serverEverStarted;
    private AtomicInteger id;
    private final File idFile;

    /**
     * Constructor for the ConnectionHandler class.
     * @param serverSocket The server socket to accept connections on.
     * @param executorService The executor service to run tasks on.
     * @param serverEverStarted A flag indicating if the server has ever been started.
     */
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

    /**
     * The run method for the Runnable interface.
     * Accepts connections and submits them to the executor service for processing.
     */
    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                logger.log("New connection from " + socket.getInetAddress().getHostAddress(), "System");
                executorService.submit(new PacketHandler(id, socket));
            } catch (IOException e) {
                if (running) {
                    logger.log("Error with network connection: " + e.getMessage(), "Error");
                }
            }
        }
        storeId();
    }

    /**
     * Stops the connection handler from accepting new connections.
     */
    public void stop() {
        this.running = false;
    }

    /**
     * Retrieves the ID from a file.
     * @return The ID as an AtomicInteger.
     */
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

    /**
     * Stores the ID to a file.
     */
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