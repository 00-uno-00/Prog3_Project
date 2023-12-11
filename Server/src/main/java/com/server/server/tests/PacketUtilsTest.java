package com.server.server.tests;

import com.server.server.models.Packet;
import com.server.server.utils.PacketUtils;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class PacketUtilsTest {

    @Test
    public void isValidSender_returnsFalse_whenSenderIsNull() {
        assertFalse(PacketUtils.isValidSender(null));
    }

    @Test
    public void isValidSender_returnsFalse_whenSenderIsEmpty() {
        assertFalse(PacketUtils.isValidSender(""));
    }

    @Test
    public void isValidSender_returnsTrue_whenSenderIsClient() {
        assertTrue(PacketUtils.isValidSender("client"));
    }

    @Test
    public void isValidSender_returnsTrue_whenSenderExistsInAccountsDirectory() {
        // Create a temporary directory and file to simulate an existing account
        File tempDir = new File("Server/src/main/resources/com/server/server/accounts/tempAccount");
        boolean dir = tempDir.mkdirs();
        assertTrue(PacketUtils.isValidSender("tempAccount"));
        boolean del = tempDir.delete();
        if (!del || !dir) {
            fail("Failed to create or delete temporary directory");
        }
    }

    @Test
    public void sendPacket_doesNotThrowException_whenPacketAndStreamAreValid() {
        try {
            Packet packet = new Packet();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            PacketUtils.sendPacket(packet, oos);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IOException.class)
    public void sendPacket_throwsException_whenStreamIsInvalid() throws IOException {
        Packet packet = new Packet();
        OutputStream os = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Stream error");
            }
        };
        ObjectOutputStream oos = new ObjectOutputStream(os);
        PacketUtils.sendPacket(packet, oos);
    }
}