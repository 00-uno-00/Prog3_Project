package com.server.server.models;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {
    private static Logger instance;
    private final File logFile;
    private final StringProperty latestLogEvent = new SimpleStringProperty();
    private final StringProperty latestLogDate = new SimpleStringProperty();
    private final StringProperty latestLogType = new SimpleStringProperty();
    private final AtomicInteger logCounter = new AtomicInteger(0);

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
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public synchronized void log(String message, String type) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String stringDate = String.valueOf(new Date());
            writer.write(message + ", " + type+ ", "+ stringDate + logCounter.incrementAndGet() + "\n");
            writer.flush();
            writer.close();
            Platform.runLater(() -> {
                latestLogEvent.set(message);
                latestLogDate.set(null); // Reset the latestLogDate since there can be duplicates
                latestLogDate.set(stringDate);
                latestLogType.set(null); // Reset the latestLogType, same reason of LogDate
                latestLogType.set(type);
            });
        } catch (Exception e) {
            System.err.println("Exception occurred while logging: " + e.getMessage());
        }
    }

    public StringProperty latestLogEventProperty() {
        return latestLogEvent;
    }

    public StringProperty latestLogDateProperty() {
        return latestLogDate;
    }

    public StringProperty latestLogTypeProperty() {
        return latestLogType;
    }
}