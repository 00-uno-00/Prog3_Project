package com.server.server.models;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Logger class for logging events.
 * This class is a singleton, meaning only one instance of this class can exist.
 */
public class Logger {
    private static Logger instance; // Singleton instance
    private final File logFile; // File where logs are written
    private StringProperty latestLogEvent = new SimpleStringProperty(); // Latest log event
    private StringProperty latestLogDate = new SimpleStringProperty(); // Date of the latest log event
    private StringProperty latestLogType = new SimpleStringProperty(); // Type of the latest log event
    private final AtomicInteger logCounter = new AtomicInteger(0); // Counter for log events

    /**
     * Private constructor for Logger class.
     * Initializes the log file in the specified directory.
     */
    private Logger() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String baseDir = System.getProperty("user.dir");
        String relativePath = "/Server/src/main/resources/com/server/server/logs/";
        File directory = new File(baseDir + relativePath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // This will create the directory if it doesn't exist
            if(created){
                System.out.println("Created directory for logs");
            }
            else{
                System.err.println("Failed to create directory for logs");
            }
        }
        logFile = new File(directory, "log_" + formatter.format(new Date()) + ".csv");
    }

    /**
     * Method to get the singleton instance of Logger.
     * If the instance doesn't exist, it is created.
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Method to log a message.
     * The message, its type and the current date are written to the log file.
     * @param message The message to log
     * @param type The type of the message
     */
    public synchronized void log(String message, String type) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String stringDate = String.valueOf(new Date());
            writer.write(message + ", " + type+ ", "+ stringDate + logCounter.incrementAndGet() + "\n");
            writer.flush();
            writer.close();
            Platform.runLater(() -> {
                latestLogEvent = new SimpleStringProperty(message);
                latestLogDate = new SimpleStringProperty(stringDate);
                latestLogType = new SimpleStringProperty(type);
            });
        } catch (Exception e) {
            System.err.println("Exception occurred while logging: " + e.getMessage());
        }
    }

    /**
     * Getter for latestLogEvent property.
     * @return latestLogEvent property
     */
    public StringProperty latestLogEventProperty() {
        return latestLogEvent;
    }

    /**
     * Getter for latestLogDate property.
     * @return latestLogDate property
     */
    public StringProperty latestLogDateProperty() {
        return latestLogDate;
    }

    /**
     * Getter for latestLogType property.
     * @return latestLogType property
     */
    public StringProperty latestLogTypeProperty() {
        return latestLogType;
    }
}