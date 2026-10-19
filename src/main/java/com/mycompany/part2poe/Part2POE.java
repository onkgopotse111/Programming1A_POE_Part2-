/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.part2poe;

import java.util.Scanner;
/**
 *
 * @author st104
 */
public class Part2POE {

public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        Login login = new Login();
        
        //Ask the user for their registration details
        System.out.println("Please enter a username:");
        String username = scan.nextLine();
        
        System.out.println("Please enter a password:");
        String password = scan.nextLine();
        
        System.out.println("Please enter a South African cell phone number (e.g. +27831234567):");
        String cellPhoneNumber = scan.nextLine();
        
        System.out.println("Please enter your first name:");
        String firstName = scan.nextLine();
        
        System.out.println("Please enter your last name:");
        String lastName = scan.nextLine();
        
        // Try to register the user with the details they entered above
        String registrationMessage = login.registerUser(username, password, cellPhoneNumber, firstName, lastName);
        System.out.println(registrationMessage);
        
        //Only continue if registration actually succeeded
        boolean registrationSuccessful = login.checkUserName(username) && login.checkPasswordComplexity(password) && login.checkCellPhoneNumber(cellPhoneNumber);
        
        if (registrationSuccessful){
            System.out.println("\n Now please log in.");
            
            System.out.println("Enter your username:");
            String loginUsername = scan.nextLine();
            
            System.out.println("Enter your password:");
            String loginPassword = scan.nextLine();
            
            boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
            String loginStatusMessage = login.returnLoginStatus(loginSuccess);
            System.out.println(loginStatusMessage);
            
            // PART 2: messages can only be sent once the user has logged in successfully
            if (loginSuccess){
                quickChatMenu(scan);
            }
        }
        
        scan.close();
    }
 
    // PART 2: the QuickChat menu, only reached after a successful login
    public static void quickChatMenu(Scanner scan) {
 
        Message message = new Message(scan);   // shares the same Scanner as the rest of the program
        boolean messagesEntered = false;
        boolean running = true;
 
        System.out.println("\nWelcome to QuickChat.");
 
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
 
            String choice = scan.nextLine().trim();
 
            switch (choice) {
                case "1":
                    if (messagesEntered) {
                        System.out.println("You have already entered the number of messages you chose.");
                    } else {
                        message.sendingMessages();
                        messagesEntered = true;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
            }
        }
    }
}
