package com.server.server.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerModel {
    private static File idFile;
    private static AtomicInteger id;
    private static Logger logger;
    private boolean serverEverStarted = false;

    private boolean isOn = false;

    public ServerModel() {
        try {
            URI uri = Objects.requireNonNull(getClass().getResource("id/")).toURI();
            idFile = new File(new File(uri), "id.txt");
            id = new AtomicInteger(0);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e+ " URI Syntax Exception");
        }
        logger = Logger.getInstance();
    }

    public synchronized AtomicInteger retrieveID() {
        try {
            if (!idFile.exists() && idFile.createNewFile()) {
                logger.log("Created Mail ID file (default: 0) ", new Date());
            }
            Scanner scanner = new Scanner(idFile);
            id.set(scanner.hasNextInt() ? scanner.nextInt() : 0);
            logger.log("Retrieved Mail ID from file (" + id.get() + ") ", new Date());
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException("Error handling id file", e);
        }
        return id;
    }

    public synchronized void storeId() {
        if (!serverEverStarted) {
            logger.log("Closing Application, won't store ID since Server never started ", new Date());
            return;
        }
        try (PrintWriter writer = new PrintWriter(idFile)) {
            writer.println(id);
            logger.log("Stored Mail ID to file (" + id.get() + ") ", new Date());
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to id file");
        }
    }

    public void startServer() {
        // Start the server here
        serverEverStarted = true;
        isOn = true;
        logger.log("Started Server ", new Date());
        id = retrieveID();
    }

    public void stopServer() { //the log is not updated with the date (sync problem?) Signleton design pattern issue?
        // Stop the server here
        storeId();
        isOn = false;
        logger.log("Stopped Server ", new Date());
    }

    public boolean isOn() {
        return isOn;
    }
}