package com.server.server.tests;

import com.server.server.models.Email;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmailTest {

    @Test
    public void isValidFormat_withValidEmail_returnsTrue() {
        assertTrue(Email.isValidFormat("a@aa.aa"));
    }

    @Test
    public void isValidFormat_withInvalidEmail_returnsFalse() {
        assertFalse(Email.isValidFormat("test@.com"));
    }

    @Test
    public void isValidFormat_withEmptyString_returnsFalse() {
        assertFalse(Email.isValidFormat(""));
    }

    @Test
    public void isValidFormat_withEmailExceeding64Characters_returnsFalse() {
        String localPart = new String(new char[65]).replace("\0", "a");
        assertFalse(Email.isValidFormat(localPart + "@example.com"));
    }

    @Test
    public void isValidFormat_withEmailContainingConsecutiveDots_returnsFalse() {
        assertFalse(Email.isValidFormat("test..test@example.com"));
    }

    @Test
    public void isValidFormat_withEmailStartingWithDot_returnsFalse() {
        assertFalse(Email.isValidFormat(".test@example.com"));
    }

    @Test
    public void isValidFormat_withEmailEndingWithDot_returnsFalse() {
        assertFalse(Email.isValidFormat("test.@example.com"));
    }
}