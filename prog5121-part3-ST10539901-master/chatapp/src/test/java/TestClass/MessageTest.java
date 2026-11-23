package TestClass;

import com.mycompany.chatapp.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Unit tests for the Message class — Part 3.
 *
 * All tests use assertEquals() and the exact test data from the POE document.
 *
 * Test Data (POE spec):
 *   Message 1: +27834557896 | "Did you get the cake?"                              | Sent
 *   Message 2: +27838884567 | "Where are you? You are late! I have asked you to be on time." | Stored
 *   Message 3: +27834484567 | "Yohoooo, I am at your gate."                        | Disregard
 *   Message 4: 0838884567   | "It is dinner time !"                                | Sent
 *   Message 5: +27838884567 | "OK, I am leaving without you."                      | Stored
 *
 * @author lab_services_student
 */
public class MessageTest {

    private Message message1; // Sent
    private Message message2; // Stored
    private Message message3; // Disregarded
    private Message message4; // Sent  (invalid recipient — no + prefix)
    private Message message5; // Stored

    @BeforeEach
    public void setUp() {
        message1 = new Message(0, "+27834557896", "Did you get the cake?");
        message2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message(2, "+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message(3, "0838884567",   "It is dinner time !");
        message5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");
    }

    // ── Message Length Tests ──────────────────────────────────────────────────

    @Test
    public void testMessageUnder250CharactersSuccess() {
        assertEquals("Message ready to send.",
            message1.checkMessageLength(),
            "Short message should pass the length check.");
    }

    @Test
    public void testMessageOver250CharactersFailure() {
        String longText = "A".repeat(270);
        Message longMsg = new Message(0, "+27123456789", longText);
        assertEquals("Message exceeds 250 characters by 20; please reduce the size.",
            longMsg.checkMessageLength(),
            "270-char message should report an excess of 20.");
    }

    // ── Recipient Cell Number Tests ───────────────────────────────────────────

    @Test
    public void testRecipientCellSuccessfullyFormatted() {
        assertEquals("Cell phone number successfully captured.",
            message1.checkRecipientCellNumber(),
            "+27834557896 is a valid international number.");
    }

    @Test
    public void testRecipientCellIncorrectlyFormatted() {
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            message4.checkRecipientCellNumber(),
            "0838884567 has no + prefix and should fail.");
    }

    // ── Message Hash Tests ────────────────────────────────────────────────────

    @Test
    public void testMessage1HashEndsWithDIDCAKE() {
        String hash    = message1.createMessageHash();
        String[] parts = hash.split(":");
        assertEquals(3, parts.length,
            "Hash must have exactly 3 colon-separated parts.");
        assertEquals("DIDCAKE", parts[2],
            "Hash third part for 'Did you get the cake?' should be DIDCAKE.");
    }

    @Test
    public void testMessage2HashEndsWithWHERETIME() {
        String hash    = message2.createMessageHash();
        String[] parts = hash.split(":");
        assertEquals("WHERETIME", parts[2],
            "Hash third part for message2 should be WHERETIME.");
    }

    @Test
    public void testMessageHashIsAllCaps() {
        String hash = message1.createMessageHash();
        assertEquals(hash.toUpperCase(), hash,
            "Message hash must be entirely uppercase.");
    }

    @Test
    public void testMessageHashHasThreeParts() {
        String hash    = message1.createMessageHash();
        String[] parts = hash.split(":");
        assertEquals(3, parts.length,
            "Hash must contain exactly 3 colon-separated parts.");
    }

    // ── Message ID Tests ──────────────────────────────────────────────────────

    @Test
    public void testMessageIDIsExactly10Characters() {
        assertEquals(10, message1.getMessageID().length(),
            "Message ID must be exactly 10 digits.");
    }

    @Test
    public void testMessageIDNotMoreThan10Characters() {
        assertEquals(true, message1.checkMessageID(),
            "checkMessageID() must return true when ID is ≤ 10 chars.");
    }

    // ── sentMessage() Action Tests ────────────────────────────────────────────

    @Test
    public void testSendMessageReturnsCorrectResponse() {
        assertEquals("Message successfully sent.",
            message1.sentMessage(1),
            "Option 1 (Send) should return 'Message successfully sent.'");
    }

    @Test
    public void testDisregardMessageReturnsCorrectResponse() {
        assertEquals("Press 0 to delete the message.",
            message3.sentMessage(2),
            "Option 2 (Disregard) should return 'Press 0 to delete the message.'");
    }

    @Test
    public void testStoreMessageReturnsCorrectResponse() {
        assertEquals("Message successfully stored.",
            message2.sentMessage(3),
            "Option 3 (Store) should return 'Message successfully stored.'");
    }

    //Sent Messages Parallel Array Test

    /**
     * Spec: Sent Messages array contains messages 1 and 4 (the two "Sent" messages).
     * Expected texts: "Did you get the cake?", "It is dinner time !"
     */
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        message1.sentMessage(1); // "Did you get the cake?"
        message4.sentMessage(1); // "It is dinner time !"

        ArrayList<String> sentTexts = Message.getSentMessageTexts();

        assertEquals(true, sentTexts.contains("Did you get the cake?"),
            "Sent array should contain 'Did you get the cake?'");
        assertEquals(true, sentTexts.contains("It is dinner time !"),
            "Sent array should contain 'It is dinner time !'");
    }

    // Longest Message Test 

    /**
     * Spec: Longest message across messages 1–4.
     * Expected: "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    public void testDisplayLongestMessage() {
        // Add all four messages to compare
        message1.sentMessage(1);
        message2.sentMessage(1);
        message3.sentMessage(1);
        message4.sentMessage(1);

        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            Message.getLongestSentMessage(),
            "getLongestSentMessage() should return message2's text.");
    }

    // ── Search by Message ID (recipient 0838884567) Test ─────────────────────

    /**
     * Spec: Enter recipient "0838884567" (message4's developer field).
     * System returns: "It is dinner time !"
     * Message4 is processed (any action), so it lands in allMessages arrays.
     */
    @Test
    public void testSearchMessageByRecipientReturnsItsdinnertime() {
        message4.sentMessage(1); // process message4 — adds to allMessages arrays

        String result = Message.searchMessageByRecipient("0838884567");

        assertEquals("It is dinner time !", result,
            "Searching by recipient '0838884567' should return 'It is dinner time !'");
    }

    // ── Search by Recipient Test ──────────────────────────────────────────────

    /**
     * Spec: Search all messages for +27838884567.
     * Expected: message2 and message5 returned.
     */
    @Test
    public void testSearchMessagesByRecipientReturnsBothMessages() {
        message2.sentMessage(1); // send message2 to +27838884567
        message5.sentMessage(1); // send message5 to +27838884567

        String result = Message.searchMessagesByRecipient("+27838884567");

        assertEquals(true,
            result.contains("Where are you? You are late! I have asked you to be on time."),
            "Result should contain message2's text.");
        assertEquals(true,
            result.contains("Ok, I am leaving without you."),
            "Result should contain message5's text.");
    }

    // Total Messages Sent Test 

    @Test
    public void testReturnTotalMessagesSentIncrementsCorrectly() {
        int before = Message.returnTotalMessages();
        new Message(0, "+27834557896", "Did you get the cake?").sentMessage(1);
        new Message(3, "+27000000000", "It is dinner time !").sentMessage(1);
        int after = Message.returnTotalMessages();
        assertEquals(before + 2, after,
            "Total sent counter should increment by 2 after two sends.");
    }
}