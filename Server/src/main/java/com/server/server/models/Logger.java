package com.server.server.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {
    private static Logger instance;
    private final File logFile;
    private final StringProperty latestLogEvent = new SimpleStringProperty();
    private final StringProperty latestLogDate = new SimpleStringProperty();
    private final AtomicInteger logCounter = new AtomicInteger(0);

    private Logger() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        logFile = new File(Objects.requireNonNull(getClass().getResource("logs/")).getPath(), "log_" + formatter.format(new Date()) + ".csv");
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public synchronized void log(String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String formattedDate = new Date() + ", " + logCounter.incrementAndGet() ;
            writer.write(message + ", " + formattedDate + "\n");
            latestLogEvent.set(message);
            latestLogDate.set(formattedDate);
        } catch (Exception e) {
            log("Exception occurred while logging: " + e.getMessage());
        }
    }

    public StringProperty latestLogEventProperty() {
        return latestLogEvent;
    }

    public StringProperty latestLogDateProperty() {
        return latestLogDate;
    }
}