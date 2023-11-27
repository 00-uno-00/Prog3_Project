package com.server.server;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Logger class for logging server events.
 */
public class Logger {
    private File logFile;

    /**
     * Constructor for Logger class.
     * Creates a new log file with a timestamp in its name.
     */
    public Logger() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        try{
            // Get the URI of the logs directory
            URI uri = Objects.requireNonNull(Logger.class.getResource("logs/")).toURI();
            // Create a new log file name with the current timestamp
            String logFileName = "log_" + formatter.format(new Date()) + ".csv";
            // Create the new log file
            logFile = new File(new File(uri), logFileName);
            System.out.println("Log file created: " + logFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error creating log file");
        }
    }

    /**
     * Logs a message to the log file.
     * This method is synchronized to prevent concurrent write access to the log file.
     * @param message The message to log.
     */
    public synchronized void log(String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            // Write the message to the log file
            writer.write(message+",\n");
        } catch (Exception e) {
            System.out.println("Error writing to log file");
        }
    }
}