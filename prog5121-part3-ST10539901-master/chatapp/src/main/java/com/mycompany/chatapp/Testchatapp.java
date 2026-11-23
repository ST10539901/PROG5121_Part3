package com.mycompany.chatapp;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application class for QuickChat.
 * Handles registration, login, sending messages, and the stored messages menu.
 *
 * @author lab_services_student
 */
public class Testchatapp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Registration 
        System.out.println("Welcome to QuickChat");
        System.out.println("Please register to continue.\n");

        System.out.print("Enter username: ");
        String registerUsername = input.nextLine();

        System.out.print("Enter password: ");
        String registerPassword = input.nextLine();

        Login user = new Login(registerUsername, registerPassword);
        System.out.println(user.registerUser());

        if (!user.checkUserName() || !user.checkPasswordComplexity()) {
            System.out.println("Registration failed. Please restart the application.");
            input.close();
            return;
        }

        //Login 
        System.out.println("\nPlease Log In");
        System.out.print("Username: ");
        String loginUsername = input.nextLine();

        System.out.print("Password: ");
        String loginPassword = input.nextLine();

        System.out.println(user.loginUser(loginUsername, loginPassword));

        if (!user.isLoggedIn()) {
            System.out.println("Login failed. Exiting.");
            input.close();
            return;
        }

        // Main Menu Loop 
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {

                //SEND MESSAGES
                case "1":
                    System.out.print("How many messages would you like to send? ");
                    int numMessages;
                    try {
                        numMessages = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Returning to menu.");
                        break;
                    }

                    for (int i = 0; i < numMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

                        System.out.print("Enter recipient cell number (e.g. +27123456789): ");
                        String recipient = input.nextLine().trim();

                        System.out.print("Enter your message (max 250 characters): ");
                        String messageText = input.nextLine();

                        Message msg = new Message(i, recipient, messageText);

                        // Validate recipient — reject and retry if invalid
                        String recipientCheck = msg.checkRecipientCellNumber();
                        System.out.println(recipientCheck);
                        if (!recipientCheck.equals("Cell phone number successfully captured.")) {
                            System.out.println("Please try again.");
                            i--;
                            continue;
                        }

                        // Validate length — reject and retry if too long
                        String lengthCheck = msg.checkMessageLength();
                        System.out.println(lengthCheck);
                        if (!lengthCheck.equals("Message ready to send.")) {
                            System.out.println("Please try again.");
                            i--;
                            continue;
                        }

                        System.out.println("Message Hash: " + msg.createMessageHash());
                        System.out.println("Message ID:   " + msg.getMessageID());

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose: ");

                        int sendChoice;
                        try {
                            sendChoice = Integer.parseInt(input.nextLine().trim());
                        } catch (NumberFormatException e) {
                            sendChoice = 2; // default to disregard
                        }

                        System.out.println(msg.sentMessage(sendChoice));
                        System.out.println("\n" + msg.formatMessage());
                    }

                    System.out.println("\nTotal messages sent this session: " + Message.returnTotalMessages());
                    break;

                //SHOW RECENTLY SENT
                case "2":
                    System.out.println(Message.printMessages());
                    break;

                //STORED MESSAGES SUB-MENU
                case "3":
                    storedMessagesMenu(input);
                    break;

                //QUIT
                case "4":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        input.close();
    }

    // Stored Messages Sub-Menu 
    
    //Displays and handles the Stored Messages sub-menu.
    
     //Uses parallel arrays for all search and display operations.
     
     //@param input shared Scanner instance
    
    private static void storedMessagesMenu(Scanner input) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message by ID");
            System.out.println("d) Search messages for a particular recipient");
            System.out.println("e) Delete a message using its hash");
            System.out.println("f) Display full report of all stored messages");
            System.out.println("g) Back to Main Menu");
            System.out.print("Choose: ");
            String sub = input.nextLine().trim().toLowerCase();

            // Load stored messages from file into array for this session
            ArrayList<Message> stored = Message.loadStoredMessages();

            switch (sub) {

                // a) Display recipient of all stored messages
                case "a":
                    if (stored.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        System.out.println("\nStored Messages — Recipients:");
                        for (Message m : stored) {
                            System.out.println("Recipient: " + m.getRecipient()
                                    + " | Message: " + m.getMessageText());
                        }
                    }
                    break;

                // b) Display longest stored message (searches stored array)
                case "b":
                    if (stored.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        Message longest = stored.get(0);
                        for (Message m : stored) {
                            if (m.getMessageText().length() > longest.getMessageText().length()) {
                                longest = m;
                            }
                        }
                        System.out.println("Longest stored message:\n" + longest.getMessageText());
                    }
                    break;

                // c) Search by message ID (searches sent parallel array)
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String searchID = input.nextLine().trim();
                    System.out.println(Message.searchMessageByID(searchID));
                    break;

                // d) Search by recipient (searches sent + stored parallel arrays)
                case "d":
                    System.out.print("Enter recipient number to search: ");
                    String searchRec = input.nextLine().trim();
                    System.out.println(Message.searchMessagesByRecipient(searchRec));
                    break;

                // e) Delete by message hash
                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String hash = input.nextLine().trim();
                    System.out.println(Message.deleteStoredMessageByHash(hash));
                    break;

                // f) Full report of all stored messages
                case "f":
                    if (stored.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        System.out.println("\n--- Full Stored Messages Report ---");
                        for (Message m : stored) {
                            System.out.println(m.formatMessage());
                            System.out.println("-----------------------------------");
                        }
                    }
                    break;

                case "g":
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
