package com.server.server.tests;

import com.server.server.models.Email;
import com.server.server.utils.AccountUtils;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.*;

public class AccountUtilsTest {

    @Test
    public void isAlreadyTaken_returnsTrue_whenAccountFolderDoesNotExist() {
        assertTrue(AccountUtils.isAlreadyTaken("nonexistentFolder"));
    }

    @Test
    public void isAlreadyTaken_returnsFalse_whenAccountFolderExists() {
        String existingFolder = "Server/src/main/resources/com/server/server/accounts/existingAccount";
        boolean dir = new File(existingFolder).mkdirs();
        assertFalse(AccountUtils.isAlreadyTaken(existingFolder));
        boolean del = new File(existingFolder).delete();
        if(!dir || !del) {
            fail("Error creating or deleting directory.");
        }
    }

    @Test
    public void createAccountFolder_returnsTrue_whenDirectoryIsCreatedSuccessfully() {
        String newFolder = "Server/src/main/resources/com/server/server/accounts/newAccount";
        assertTrue(AccountUtils.createAccountFolder(newFolder));
        boolean del = new File(newFolder).delete();
        if(!del) {
            fail("Error deleting directory.");
        }
    }

    @Test
    public void initializeAccountFolder_returnsTrue_whenInitializationIsSuccessful() {
        String newFolder = "Server/src/main/resources/com/server/server/accounts/newAccount";
        AccountUtils.createAccountFolder(newFolder);
        assertTrue(AccountUtils.initializeAccountFolder(newFolder));
        boolean del = new File(newFolder + "/emails.json").delete();
        boolean folderDel = new File(newFolder).delete();
        if(!del || !folderDel) {
            fail("Error deleting directory or emails.json file.");
        }
    }

    @Test
    public void doesAccountExist_returnsFalse_whenAccountFolderExists() {
        String existingFolder = "Server/src/main/resources/com/server/server/accounts/existingAccount";
        boolean dir = new File(existingFolder).mkdirs();
        assertFalse(AccountUtils.doesAccountExist(existingFolder));
        boolean del = new File(existingFolder).delete();
        if (!dir || !del) {
            fail("Error creating or deleting directory.");
        }
    }

    @Test
    public void isAccountInitialized_returnsTrue_whenAccountIsInitialized() {
        String newFolder = "Server/src/main/resources/com/server/server/accounts/newAccount";
        AccountUtils.createAccountFolder(newFolder);
        AccountUtils.initializeAccountFolder(newFolder);
        assertTrue(AccountUtils.isAccountInitialized(newFolder));
        boolean del = new File(newFolder + "/emails.json").delete();
        boolean folderDel = new File(newFolder).delete();
        if(!del || !folderDel) {
            fail("Error deleting directory or emails.json file.");
        }
    }

    @Test
    public void retrieveEmails_returnsEmptyHashMap_whenNoEmailsExist() {
        String newFolder = "Server/src/main/resources/com/server/server/accounts/newAccount";
        AccountUtils.createAccountFolder(newFolder);
        AccountUtils.initializeAccountFolder(newFolder);
        assertTrue(AccountUtils.retrieveEmails(newFolder).isEmpty());
        boolean del = new File(newFolder + "/emails.json").delete();
        boolean folderDel = new File(newFolder).delete();
        if(!del || !folderDel) {
            fail("Error deleting directory or emails.json file.");
        }
    }

    @Test
    public void storeEmails_returnsTrue_whenEmailsAreStoredSuccessfully() {
        String newFolder = "Server/src/main/resources/com/server/server/accounts/newAccount";
        AccountUtils.createAccountFolder(newFolder);
        AccountUtils.initializeAccountFolder(newFolder);
        HashMap<Integer, Email> emails = new HashMap<>();
        emails.put(1, new Email());
        assertTrue(AccountUtils.storeEmails(newFolder, emails));
        boolean del = new File(newFolder + "/emails.json").delete();
        boolean folderDel = new File(newFolder).delete();
        if(!del || !folderDel) {
            fail("Error deleting directory or emails.json file.");
        }
    }

    @Test
    public void getAccountFolder_returnsCorrectPath() {
        assertEquals("Server/src/main/resources/com/server/server/accounts/testUser", AccountUtils.getAccountFolder("testUser"));
    }
}