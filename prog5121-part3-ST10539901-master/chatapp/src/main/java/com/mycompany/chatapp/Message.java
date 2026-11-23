package com.mycompany.chatapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Message class for QuickChat application.
 * Manages sent, stored, and disregarded messages using parallel arrays.
 * Uses only standard Java libraries - no external JARs required.
 *
 * @author lab_services_student
 */
public class Message {

    // Instance fields 
    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status; // "Sent" | "Stored" | "Disregarded"

    //Parallel arrays (shared across all Message instances)
    // Sent messages
    private static ArrayList<String> sentMessageTexts      = new ArrayList<>();
    private static ArrayList<String> sentMessageRecipients = new ArrayList<>();
    private static ArrayList<String> sentMessageHashes     = new ArrayList<>();
    private static ArrayList<String> sentMessageIDs        = new ArrayList<>();

    // Disregarded messages
    private static ArrayList<String> disregardedTexts      = new ArrayList<>();
    private static ArrayList<String> disregardedRecipients = new ArrayList<>();

    // ALL messages regardless of status (sent, stored, disregarded)
    private static ArrayList<String> allMessageTexts       = new ArrayList<>();
    private static ArrayList<String> allMessageRecipients  = new ArrayList<>();
    private static ArrayList<String> allMessageHashes      = new ArrayList<>();
    private static ArrayList<String> allMessageIDs         = new ArrayList<>();

    // Total sent counter
    private static int totalMessagesSent = 0;

    // JSON file path for stored messages
    private static final String JSON_FILE = "stored_messages.json";

    //  Constructor 
    /**
     * Creates a new Message and auto-generates an ID and hash.
     *
     * @param messageNumber the sequence number of this message
     * @param recipient     the recipient's cell number (e.g. +27834557896)
     * @param messageText   the message body
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    //  ID Generation
    /**
     * Generates a random 10-digit numeric string as the message ID.
     */
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // Validation Methods 
    /**
     * Checks that the message ID does not exceed 10 characters.
     */
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    /**
     * Validates that the recipient number starts with '+' and is at most 13 chars.
     */
    public String checkRecipientCellNumber() {
        if (recipient.startsWith("+") && recipient.length() <= 13) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Validates that the message does not exceed 250 characters.
     */
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = messageText.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    //Hash Creation 
    /**
     * Creates a message hash in the format:
     *   XX:N:FIRSTWORDLASTWORD  (all caps, letters and digits only)
     * Example: "Hi Mike, see you tonight." → XX:0:HITONIGHT
     */
    public String createMessageHash() {
        String idPrefix  = messageID.substring(0, 2);
        String[] words   = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        messageHash = (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    //Message Action 
    /**
     * Processes the user's choice for this message.
     *
     * @param choice 1 = Send, 2 = Disregard, 3 = Store
     * @return result message string
     */
    public String sentMessage(int choice) {
        // Every message goes into the all-messages parallel arrays regardless of action
        allMessageTexts.add(this.messageText);
        allMessageRecipients.add(this.recipient);
        allMessageHashes.add(this.messageHash);
        allMessageIDs.add(this.messageID);

        switch (choice) {
            case 1: // Send
                status = "Sent";
                sentMessageTexts.add(this.messageText);
                sentMessageRecipients.add(this.recipient);
                sentMessageHashes.add(this.messageHash);
                sentMessageIDs.add(this.messageID);
                totalMessagesSent++;
                return "Message successfully sent.";

            case 2: // Disregard
                status = "Disregarded";
                disregardedTexts.add(this.messageText);
                disregardedRecipients.add(this.recipient);
                return "Press 0 to delete the message.";

            case 3: // Store
                status = "Stored";
                saveMessageToFile(this);
                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    // Parallel Array Search Methods
    /**
     * Finds the longest message across the sent messages parallel array.
     *
     * @return the text of the longest sent message, or a not-found message
     */
    public static String getLongestSentMessage() {
        if (sentMessageTexts.isEmpty()) {
            return "No sent messages found.";
        }
        String longest = sentMessageTexts.get(0);
        for (String text : sentMessageTexts) {
            if (text.length() > longest.length()) {
                longest = text;
            }
        }
        return longest;
    }

    /**
     * Searches ALL messages (sent, stored, disregarded) by recipient number.
     * Used for the spec test: enter 0838884567, returns "It is dinner time !"
     *
     * @param searchRecipient the recipient number to search for
     * @return matching message text, or not-found string
     */
    public static String searchMessageByRecipient(String searchRecipient) {
        for (int i = 0; i < allMessageRecipients.size(); i++) {
            if (allMessageRecipients.get(i).equals(searchRecipient)) {
                return allMessageTexts.get(i);
            }
        }
        return "No message found for recipient: " + searchRecipient;
    }

    /**
     * Searches ALL messages by message ID and returns recipient + message text.
     *
     * @param searchID the message ID to look up
     * @return formatted recipient and message, or not-found string
     */
    public static String searchMessageByID(String searchID) {
        for (int i = 0; i < allMessageIDs.size(); i++) {
            if (allMessageIDs.get(i).equals(searchID)) {
                return "Recipient: " + allMessageRecipients.get(i)
                     + "\nMessage: "  + allMessageTexts.get(i);
            }
        }
        return "No message found with ID: " + searchID;
    }

    /**
     * Searches sent AND stored messages for all messages sent to a given recipient.
     * Used for the spec test: enter +27838884567, returns messages 2 and 5.
     *
     * @param searchRecipient the recipient number to search for
     * @return all matching message texts, or not-found string
     */
    public static String searchMessagesByRecipient(String searchRecipient) {
        StringBuilder result = new StringBuilder();

        // Search sent parallel array
        for (int i = 0; i < sentMessageRecipients.size(); i++) {
            if (sentMessageRecipients.get(i).equals(searchRecipient)) {
                result.append(sentMessageTexts.get(i)).append("\n");
            }
        }

        // Search stored messages from file
        ArrayList<Message> stored = loadStoredMessages();
        for (Message m : stored) {
            if (m.recipient.equals(searchRecipient)) {
                result.append(m.messageText).append("\n");
            }
        }

        if (result.length() == 0) {
            return "No messages found for recipient: " + searchRecipient;
        }
        return result.toString().trim();
    }

    // File Storage (plain Java — no external library) 
    /**
     * Saves a message to the JSON file by loading existing messages,
     * adding the new one, and rewriting the file.
     */
    private static void saveMessageToFile(Message msg) {
        ArrayList<Message> existing = loadStoredMessages();
        existing.add(msg);
        writeAllToFile(existing);
    }

    /**
     * Writes the full list of stored messages to the JSON file.
     */
    private static void writeAllToFile(ArrayList<Message> messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            sb.append("  {\n");
            sb.append("    \"messageID\": \"").append(escapeJson(m.messageID)).append("\",\n");
            sb.append("    \"messageNumber\": ").append(m.messageNumber).append(",\n");
            sb.append("    \"recipient\": \"").append(escapeJson(m.recipient)).append("\",\n");
            sb.append("    \"messageText\": \"").append(escapeJson(m.messageText)).append("\",\n");
            sb.append("    \"messageHash\": \"").append(escapeJson(m.messageHash)).append("\",\n");
            sb.append("    \"status\": \"Stored\"\n");
            sb.append("  }");
            if (i < messages.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_FILE))) {
            bw.write(sb.toString());
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Escapes characters that would break JSON string values.
     */
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Reads stored_messages.json and returns an ArrayList of Message objects.
     * Parses manually — no external library needed.
     */
    public static ArrayList<Message> loadStoredMessages() {
        ArrayList<Message> stored = new ArrayList<>();
        StringBuilder raw = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(JSON_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                raw.append(line).append("\n");
            }
        } catch (Exception e) {
            return stored; // file doesn't exist yet — return empty list
        }

        String content = raw.toString().trim();
        if (content.isEmpty() || content.equals("[]")) return stored;

        // Split on '{' to get individual object blocks
        String[] blocks = content.split("\\{");
        for (String block : blocks) {
            block = block.trim();
            if (block.isEmpty() || block.startsWith("[")) continue;

            String id     = extractJsonValue(block, "messageID");
            String numStr = extractJsonValue(block, "messageNumber");
            String rec    = extractJsonValue(block, "recipient");
            String text   = extractJsonValue(block, "messageText");
            String hash   = extractJsonValue(block, "messageHash");

            if (id == null || rec == null || text == null) continue;

            int num = 0;
            try { num = Integer.parseInt(numStr); } catch (Exception ignored) {}

            Message m     = new Message(num, rec, text);
            m.messageID   = id;
            m.messageHash = hash;
            m.status      = "Stored";
            stored.add(m);
        }
        return stored;
    }

    /**
     * Extracts a value from a raw JSON object block string by key name.
     */
    private static String extractJsonValue(String block, String key) {
        String search = "\"" + key + "\"";
        int keyIndex  = block.indexOf(search);
        if (keyIndex == -1) return null;

        int colon = block.indexOf(":", keyIndex + search.length());
        if (colon == -1) return null;

        int start = colon + 1;
        while (start < block.length() && Character.isWhitespace(block.charAt(start))) start++;
        if (start >= block.length()) return null;

        if (block.charAt(start) == '"') {
            // Quoted string value
            int end = start + 1;
            StringBuilder result = new StringBuilder();
            while (end < block.length()) {
                char c = block.charAt(end);
                if (c == '\\' && end + 1 < block.length()) {
                    char next = block.charAt(end + 1);
                    if (next == '"')       result.append('"');
                    else if (next == '\\') result.append('\\');
                    else                   result.append(next);
                    end += 2;
                } else if (c == '"') {
                    break;
                } else {
                    result.append(c);
                    end++;
                }
            }
            return result.toString();
        } else {
            // Bare number value
            int end = start;
            while (end < block.length() && block.charAt(end) != ','
                    && block.charAt(end) != '\n' && block.charAt(end) != '}') end++;
            return block.substring(start, end).trim();
        }
    }

    /**
     * Deletes a stored message matching the given hash and rewrites the file.
     *
     * @param hash the message hash to search for
     * @return confirmation or error string
     */
    public static String deleteStoredMessageByHash(String hash) {
        ArrayList<Message> stored  = loadStoredMessages();
        ArrayList<Message> updated = new ArrayList<>();
        boolean found   = false;
        String  deleted = "";

        for (Message m : stored) {
            if (m.messageHash.equalsIgnoreCase(hash)) {
                found   = true;
                deleted = m.messageText;
            } else {
                updated.add(m);
            }
        }

        if (!found) return "Message hash not found.";
        writeAllToFile(updated);
        return "Message: \"" + deleted + "\" successfully deleted.";
    }

    //Display Methods
    /**
     * Returns a formatted report of all sent messages from the parallel arrays.
     */
    public static String printMessages() {
        if (sentMessageTexts.isEmpty()) return "No messages sent yet.";

        StringBuilder sb = new StringBuilder("\nAll Sent Messages\n");
        sb.append("------------------\n");
        for (int i = 0; i < sentMessageTexts.size(); i++) {
            sb.append("Message ID:   ").append(sentMessageIDs.get(i)).append("\n");
            sb.append("Message Hash: ").append(sentMessageHashes.get(i)).append("\n");
            sb.append("Recipient:    ").append(sentMessageRecipients.get(i)).append("\n");
            sb.append("Message:      ").append(sentMessageTexts.get(i)).append("\n");
            sb.append("------------------\n");
        }
        return sb.toString();
    }

    /**
     * Returns a formatted string of this message's details.
     */
    public String formatMessage() {
        return "Message ID: "     + messageID   +
               "\nMessage Hash: " + messageHash +
               "\nRecipient: "    + recipient   +
               "\nMessage: "      + messageText;
    }

    //Static Getters 
    public static int returnTotalMessages()                    { 
        return totalMessagesSent;   
    }
    public static ArrayList<String> getSentMessageTexts()      { 
        return sentMessageTexts;       
    }
    public static ArrayList<String> getSentMessageRecipients() { 
        return sentMessageRecipients; 
    }
    public static ArrayList<String> getSentMessageHashes()     { 
        return sentMessageHashes;  
    }
    public static ArrayList<String> getSentMessageIDs()        { 
        return sentMessageIDs;        
    }
    public static ArrayList<String> getAllMessageTexts()        { 
        return allMessageTexts;      
    }
    public static ArrayList<String> getAllMessageRecipients()   { 
        return allMessageRecipients;  
    }
    public static ArrayList<String> getAllMessageHashes()       { 
        return allMessageHashes;      
    }
    public static ArrayList<String> getAllMessageIDs()          {
        return allMessageIDs;       
    }

    // Instance Getters 
    public String getMessageID()     { 
        return messageID;    
    }
    public String getMessageHash()   { 
        return messageHash;  
    }
    public String getRecipient()     {
        return recipient;   
    }
    public String getMessageText()   {
        return messageText;  
    }
    public String getStatus()        { 
        return status;     
    }
    public int getMessageNumber() {
        return messageNumber; 
    }
}