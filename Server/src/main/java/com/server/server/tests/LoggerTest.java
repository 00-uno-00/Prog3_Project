package com.server.server.tests;

import com.server.server.models.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class LoggerTest {
    @BeforeClass
    public static void initJavaFx() {
        new Thread(() -> TestApplication.main(new String[0])).start();
    }

    @Test
    public void getInstance_createsNewInstance_whenNoneExists() throws NoSuchFieldException, IllegalAccessException {
        // Reset the singleton instance for this test
        Field instance = Logger.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        assertNotNull(Logger.getInstance());
    }

    @Test
    public void getInstance_returnsExistingInstance_whenOneExists() {
        Logger firstCall = Logger.getInstance();
        Logger secondCall = Logger.getInstance();

        assertSame(firstCall, secondCall);
    }

    @Test
    public void log_incrementsLogCounter() throws NoSuchFieldException, IllegalAccessException {
        Logger logger = Logger.getInstance();
        Field counter = Logger.class.getDeclaredField("logCounter");
        counter.setAccessible(true);

        int initialCount = ((AtomicInteger) counter.get(logger)).get();
        logger.log("Test message", "INFO");

        int finalCount = ((AtomicInteger) counter.get(logger)).get();
        assertEquals(initialCount + 1, finalCount);
    }

    @Test
    public void log_createsLogFile_ifNotExists() {
        Logger logger = Logger.getInstance();
        logger.log("Test message", "INFO");

        File logFile = new File(System.getProperty("user.dir") + "/Server/src/main/resources/com/server/server/logs/");
        assertTrue(logFile.exists());
    }

    @Test
    public void log_updatesLatestLogEvent() throws InterruptedException {
        Logger logger = Logger.getInstance();
        String message = "Test message";

        logger.log(message, "INFO");
        Thread.sleep(100); // Wait for the Platform.runLater() to finish
        assertEquals(message, logger.latestLogEventProperty().get());
    }

    @Test
    public void log_updatesLatestLogType() throws InterruptedException {
        Logger logger = Logger.getInstance();
        String type = "INFO";

        logger.log("Test message", type);
        Thread.sleep(100); // Wait for the Platform.runLater() to finish
        assertEquals(type, logger.latestLogTypeProperty().get());
    }
}