package com.server.server.models;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Logger {
    private static Logger instance = null;
    private File logFile;
    private final StringProperty latestLogEvent = new SimpleStringProperty();

    private final StringProperty latestLogDate = new SimpleStringProperty();

    private final SimpleDateFormat formatter;

    private Logger() {
        formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        try{
            URI uri = Objects.requireNonNull(getClass().getResource("logs/")).toURI();
            String logFileName = "log_" + formatter.format(new Date()) + ".csv";
            logFile = new File(new File(uri), logFileName);
            System.out.println("Log file created: " + logFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error creating log file");
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public synchronized void log(String message, Date date) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String formattedDate = formatter.format(date);
            writer.write(message + ", " + formattedDate + "\n");
            latestLogEvent.set(message);
            latestLogDate.set(formattedDate);
        } catch (Exception e) {
            System.out.println("Error writing to log file");
        }
    }

    public StringProperty latestLogEventProperty() {
        return latestLogEvent;
    }

    public StringProperty latestLogDateProperty() {
        return latestLogDate;
    }
}