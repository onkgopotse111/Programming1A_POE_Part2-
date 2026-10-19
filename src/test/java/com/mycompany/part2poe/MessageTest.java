/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.part2poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 *
 * @author st104
 */
public class MessageTest {
 
    // Test data from the assignment
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";
 
    public MessageTest() {
    }
 
    @Test
    public void messageLength_success() {
        Message msg = new Message();
        assertEquals("Message ready to send.", msg.checkMessageLength("a".repeat(250)));
    }
 
    @Test
    public void messageLength_failure() {
        Message msg = new Message();
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                msg.checkMessageLength("a".repeat(260)));
    }
 
    @Test
    public void recipientNumber_success() {
        Message msg = new Message();
        assertEquals("Cell phone number successfully captured.", msg.recipientCellStatus(RECIPIENT_1));
    }
 
    @Test
    public void recipientNumber_failure() {
        Message msg = new Message();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.recipientCellStatus(RECIPIENT_2));
    }
 
    @Test
    public void messageHash_testCase1() {
        Message msg = new Message();
        assertEquals("00:0:HITONIGHT", msg.createMessageHash("0012345678", 0, MESSAGE_1));
    }
 
    @Test
    public void messageHash_remainingMessagesInLoop() {
        String[] ids = {"0012345678", "0898765432", "1234567890"};
        String[] texts = {MESSAGE_1, MESSAGE_2, "Okay."};
        String[] expected = {"00:0:HITONIGHT", "08:1:HIPAYMENT", "12:2:OKAYOKAY"};
 
        Message msg = new Message();
        for (int i = 0; i < ids.length; i++) {
            assertEquals(expected[i], msg.createMessageHash(ids[i], i, texts[i]));
        }
    }
 
    @Test
    public void messageID_isCreated() {
        Message msg = new Message();
        String id = msg.generateMessageID();
        assertTrue(msg.checkMessageID(id));
        assertEquals(10, id.length());
        assertEquals("Message ID generated: " + id, msg.getMessageIDStatus(id));
    }
 
    @Test
    public void messageID_tooLongFails() {
        Message msg = new Message();
        assertFalse(msg.checkMessageID("12345678901"));
    }
 
    @Test
    public void messageSent_allThreeOptions() {
        Message msg = new Message();
        assertEquals("Message successfully sent.", msg.sentMessageResult(1));
        assertEquals("Press 0 to delete the message.", msg.sentMessageResult(2));
        assertEquals("Message successfully stored.", msg.sentMessageResult(3));
    }
 
    @Test
    public void storeMessage_writesJson() throws IOException {
        Message msg = new Message();
        Path file = Files.createTempFile("messages", ".json");
        String result = msg.storeMessage("0012345678", "00:0:HITONIGHT", RECIPIENT_1, MESSAGE_1, file.toString());
 
        assertEquals("Message successfully stored.", result);
        String json = Files.readString(file);
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        Files.deleteIfExists(file);
    }
 
    @Test
    public void totalMessages_sendOneDiscardOne() {
        // Scripted keyboard input: 2 messages; message 1 = Send (1); message 2 = Disregard (2) then 0 to delete
        String keyboard = "2\n"
                + RECIPIENT_1 + "\n" + MESSAGE_1 + "\nSam\n1\n"
                + "+27831234567\n" + MESSAGE_2 + "\nSam\n2\n0\n";
 
        InputStream original = System.in;
        try {
            System.setIn(new ByteArrayInputStream(keyboard.getBytes()));
            Message msg = new Message();      // reads from the scripted input
            msg.sendingMessages();
            assertEquals(1, msg.returnTotalMessages());
        } finally {
            System.setIn(original);
        }
    }
}
 