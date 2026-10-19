/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.part2poe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author st104
 */
public class Message {
     // ===================== Message data and core sending logic =====================
 
    ArrayList<String> sentMessages = new ArrayList<>();
    ArrayList<String> messageIdsList = new ArrayList<>();
    HashSet<String> messageIds = new HashSet<>();
    ArrayList<String> msgHash = new ArrayList<>();
    ArrayList<String> recipientNumber = new ArrayList<>();
    ArrayList<String> messageStatus = new ArrayList<>();
    ArrayList<String> disregardedMessages = new ArrayList<>();
    ArrayList<String> storedMessages = new ArrayList<>();
    ArrayList<String> senders = new ArrayList<>();
 
    Random random = new Random();
    Scanner input = new Scanner(System.in);
 
    int numMessageSent = 0;
 
    // Constructors: the second one lets Registration share its Scanner so two Scanners never compete for System.in
    public Message() {
    }
 
    public Message(Scanner sharedScanner) {
        this.input = sharedScanner;
    }
 
    public void sendingMessages() {
 
        System.out.print("How many messages do you want to send? ");
        int numberOfMessages = input.nextInt();
 
        input.nextLine();
 
        for (int i = 1; i <= numberOfMessages; i++) {
 
            System.out.println("\n==============================");
            System.out.println("MESSAGE " + i);
            System.out.println("==============================");
 
            String messageID = generateMessageID();
 
            System.out.print("Enter recipient cell number: ");
            String recipient = input.nextLine();
 
            String recipientResult = checkRecipientCell(recipient);
 
            if (!recipientResult.equals("Valid number")) {
                System.out.println(recipientResult);
                i--;
                continue;
            }
 
            System.out.print("Enter your message: ");
            String messageText = input.nextLine();
 
            if (messageText.length() > 250 || messageText.trim().isEmpty()) {
                System.out.println("Please enter a message of 1 to 250 characters.");
                i--;
                continue;
            }
 
            int messageNumber = sentMessages.size() + 1;
 
            String messageHash = createMessageHash(messageID, messageNumber, messageText);
 
            msgHash.add(messageHash);
            sentMessages.add(messageText);
            messageIdsList.add(messageID);
            messageIds.add(messageID);
            recipientNumber.add(recipient);
 
            System.out.print("Enter sender name: ");
            String sender = input.nextLine();
            senders.add(sender);
 
            String status = SentMessage();
 
            messageStatus.add(status);
 
            if (status.equals("Sent")) {
                numMessageSent++;
            }
        }
 
        printMessages();
 
        System.out.println(
                "Total messages sent: " + returnTotalMessages()
        );
    }
 
    public int returnTotalMessages() {
 
        return numMessageSent;
    }
 
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
 
        String firstTwoNumbers = messageID.substring(0, 2);
 
        String cleanMessage = messageText.trim();
 
        String[] words = cleanMessage.split("\\s+");
 
        // Strip punctuation from the first and last word so "tonight?" becomes TONIGHT
        // (the required hash format is, for example, 00:0:HITONIGHT)
        String firstWord = words[0].replaceAll("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$", "");
 
        String lastWord = words[words.length - 1].replaceAll("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$", "");
 
        String hash = firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord;
 
        return hash.toUpperCase();
    }
 
    public String generateMessageID() {
 
        String messageID;
 
        do {
 
            long number = 1000000000L + random.nextLong(9000000000L);
 
            messageID = String.valueOf(number);
 
        } while (messageIds.contains(messageID));
 
        return messageID;
    }
 
    public String checkRecipientCell(String cellphone) {
 
        if (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$")) {
 
            return "Valid number";
 
        } else {
 
            return "Invalid number";
        }
    }
 
    public String SentMessage() {
 
        System.out.println("\nChoose what you want to do with this message!");
 
        System.out.println("1. Send");
        System.out.println("2. Disregard");
        System.out.println("3. Store");
 
        System.out.println("Enter your choice: ");
        int choice = input.nextInt();
 
        input.nextLine();
 
        switch (choice) {
 
            case 1 -> {
                System.out.println("Message successfully sent");
                return "Sent";
            }
 
            case 2 -> {
 
                System.out.println("Press 0 to delete the message");
                int deleteChoice = input.nextInt();
 
                input.nextLine();
 
                if (deleteChoice == 0) {
 
                    System.out.println("Message deleted");
 
                    int lastIndex = sentMessages.size() - 1;
 
                    String messageID = messageIdsList.get(lastIndex);
                    messageIds.remove(messageID);
 
                    recipientNumber.remove(lastIndex);
                    sentMessages.remove(lastIndex);
                    msgHash.remove(lastIndex);
                    messageIdsList.remove(lastIndex);
                    senders.remove(lastIndex);
 
                    return "Disregarded";
 
                } else {
 
                    System.out.println("Invalid option!");
 
                    return "Disregarded";
                }
            }
 
            case 3 -> {
 
                // Save the message to the JSON file
                int lastIndex = sentMessages.size() - 1;
                storeMessage(messageIdsList.get(lastIndex), msgHash.get(lastIndex),
                        recipientNumber.get(lastIndex), sentMessages.get(lastIndex), "messages.json");
 
                System.out.println("Message successfully stored");
 
                return "Stored";
            }
 
            default -> {
 
                System.out.println("Invalid option");
 
                return "Disregarded";
            }
        }
    }
 
    public String printMessages() {
 
        StringBuilder allMessages = new StringBuilder();
 
        System.out.println("\n===== All Messages =====");
 
        for (int i = 0; i < sentMessages.size(); i++) {
 
            int messageNumber = i + 1;
 
            allMessages.append("\nMessage Number: ").append(messageNumber);
            allMessages.append("\nMessage ID: ").append(messageIdsList.get(i));
            allMessages.append("\nRecipient: ").append(recipientNumber.get(i));
            allMessages.append("\nMessage: ").append(sentMessages.get(i));
            allMessages.append("\nMessage Hash: ").append(msgHash.get(i));
            allMessages.append("\nStatus: ").append(messageStatus.get(i));
 
            System.out.println("\n==============================");
            System.out.println("\nMessage Number: " + messageNumber);
            System.out.println("Message ID: " + messageIdsList.get(i));
            System.out.println("Recipient: " + recipientNumber.get(i));
            System.out.println("Message: " + sentMessages.get(i));
            System.out.println("Message Hash: " + msgHash.get(i));
            System.out.println("Status: " + messageStatus.get(i));
        }
 
        return allMessages.toString();
    }
 
    // ===================== Validation, status messages and JSON storage =====================
 
    // Ensures the message ID is not more than ten characters
    public boolean checkMessageID(String messageID) {
        return messageID != null && !messageID.isEmpty() && messageID.length() <= 10;
    }
 
    // "Message ID generated: <Message ID>" (used by the Message ID unit test)
    public String getMessageIDStatus(String messageID) {
        return "Message ID generated: " + messageID;
    }
 
    // Success / failure wording for the recipient number, reusing checkRecipientCell above
    public String recipientCellStatus(String cellphone) {
        if (checkRecipientCell(cellphone).equals("Valid number")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }
 
    // Success / failure wording for the 250 character limit
    public String checkMessageLength(String messageText) {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int tooLong = messageText.length() - 250;
        return "Message exceeds 250 characters by " + tooLong + "; please reduce the size.";
    }
 
    // The three messages for the Send / Disregard / Store choice (1, 2 or 3), used by the unit tests
    public String sentMessageResult(int choice) {
        switch (choice) {
            case 1:
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }
 
    // Stores a message in a JSON file. All messages stored so far are written as a JSON array.
    public String storeMessage(String messageID, String hash, String recipient, String text, String filePath) {
 
        String jsonObject = "  {\n"
                + "    \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(hash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(text) + "\"\n"
                + "  }";
 
        storedMessages.add(jsonObject);
 
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            json.append(storedMessages.get(i));
            json.append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        json.append("]");
 
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        } catch (IOException e) {
            return "Message could not be stored: " + e.getMessage();
        }
 
        return "Message successfully stored.";
    }
 
    // Escapes characters that are not allowed inside a JSON string
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}
