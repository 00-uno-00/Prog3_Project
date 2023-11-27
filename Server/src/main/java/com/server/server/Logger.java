package com.server.server;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Logger {
    private File logFile;

    public Logger() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        try{
            URI uri = Objects.requireNonNull(Logger.class.getResource("logs/")).toURI();
            String logFileName = "log_" + formatter.format(new Date()) + ".csv";
            logFile = new File(new File(uri), logFileName);
            System.out.println("Log file created: " + logFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error creating log file");
        }
    }

    public synchronized void log(String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(message+",\n");
        } catch (Exception e) {
            System.out.println("Error writing to log file");
        }
    }
}