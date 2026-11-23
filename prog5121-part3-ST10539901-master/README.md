# QuickChat — Part 3: Store Data and Display Task

## Module: Programming 1A (PROG5121/p/w)

---

## Overview

Part 3 is the final extension of the QuickChat messaging application. It builds on the
Login and Message functionality from Parts 1 and 2 by adding persistent JSON file storage,
parallel arrays for message management, a Stored Messages menu with six sub-features,
and a full suite of automated unit tests.

---

## Features Added in Part 3

### 1. Parallel Arrays
All sent, stored, and disregarded messages are tracked using parallel ArrayLists that share
the same index across:

| Array | Contents |
|---|---|
| `sentMessageTexts` | The body of every sent message |
| `sentMessageRecipients` | The recipient of every sent message |
| `sentMessageHashes` | The hash of every sent message |
| `sentMessageIDs` | The ID of every sent message |
| `allMessageHashes` | Hashes for all messages (sent + stored) |
| `allMessageIDs` | IDs for all messages (sent + stored) |

### 2. JSON File Storage (No External Libraries)
Stored messages are saved to `stored_messages.json` using only standard Java libraries
(`java.io.BufferedReader`, `java.io.BufferedWriter`, `java.io.FileReader`, `java.io.FileWriter`).
No external JAR files are required — the project compiles cleanly in any standard NetBeans installation.

### 3. Stored Messages Menu (Main Menu Option 3)
A fourth main menu option gives access to the following sub-features:

| Option | Feature |
|---|---|
| a | Display the recipient of all stored messages |
| b | Display the longest stored message |
| c | Search for a message by ID |
| d | Search all messages for a particular recipient |
| e | Delete a message using its hash |
| f | Display a full report of all stored messages |
| g | Return to the Main Menu |

---

## Test Data (POE Specification)

| # | Recipient | Message | Flag |
|---|---|---|---|
| 1 | +27834557896 | "Did you get the cake?" | Sent |
| 2 | +27838884567 | "Where are you? You are late! I have asked you to be on time." | Stored |
| 3 | +27834484567 | "Yohoooo, I am at your gate." | Disregard |
| 4 | 0838884567 | "It is dinner time !" | Sent |
| 5 | +27838884567 | "Ok, I am leaving without you." | Stored |

---

## Unit Tests (MessageTest.java)

All tests use `assertEquals()` and the exact test data from the POE document.

| Test | Expected Result |
|---|---|
| Message under 250 characters | `"Message ready to send."` |
| Message over 250 characters | Reports exact character excess |
| Valid recipient cell number | `"Cell phone number successfully captured."` |
| Invalid recipient cell number | Error message returned |
| Message hash — all caps | Hash equals its own `.toUpperCase()` |
| Message hash — 3 parts | `hash.split(":").length == 3` |
| Message 1 hash third part | `"DIDCAKE"` |
| Message 2 hash third part | `"WHERETIME"` |
| Message ID is 10 digits | `id.length() == 10` |
| Message ID not more than 10 chars | `checkMessageID()` returns `true` |
| Send returns correct response | `"Message successfully sent."` |
| Disregard returns correct response | `"Press 0 to delete the message."` |
| Store returns correct response | `"Message successfully stored."` |
| Sent array correctly populated | Contains `"Did you get the cake?"` and `"It is dinner time !"` |
| Longest message | `"Where are you? You are late! I have asked you to be on time."` |
| Search by message ID | Returns `"It is dinner time !"` |
| Search by recipient +27838884567 | Returns messages 2 and 5 |
| Total messages increments correctly | Counter increases by 2 after two sends |

---

## Project Structure

```
QuickChat/
│
├── src/
│   ├── com/mycompany/chatapp/
│   │   ├── Login.java          — Registration and login logic
│   │   ├── Message.java        — Message logic, parallel arrays, file storage
│   │   └── Testchatapp.java    — Main application entry point
│   │
│   └── TestClass/
│       ├── LoginTest.java      — Unit tests for Login class
│       └── MessageTest.java    — Unit tests for Message class
│
└── stored_messages.json        — Auto-generated when messages are stored
```

---

## How to Run

### Prerequisites
- Java JDK 8 or higher
- Apache NetBeans IDE
- JUnit 5 (included in NetBeans by default)
- **No external JAR files required**

### Steps
1. Open NetBeans and load the `QuickChat` project
2. Right-click the project → **Clean and Build**
3. Right-click `Testchatapp.java` → **Run File**
4. Follow the prompts:
   - Register with a valid username (must contain `_`, max 5 characters)
   - Register with a valid password (min 8 chars, 1 capital, 1 number, 1 special character)
   - Log in with the same credentials
   - Use the Main Menu to send, store, or manage messages

### Running Unit Tests
1. Right-click `MessageTest.java` or `LoginTest.java` → **Test File**
2. All tests should pass with a green bar in the Test Results panel

---

## Validation Rules

### Username
- Must contain an underscore (`_`)
- Must be 5 characters or fewer

### Password
- Minimum 8 characters
- At least one capital letter
- At least one number
- At least one special character

### Recipient Cell Number
- Must start with `+` (international code required)
- Must be 13 characters or fewer

### Message
- Must not exceed 250 characters

### Message Hash Format
```
XX:N:FIRSTWORDLASTWORD
```
- `XX` — first two digits of the Message ID
- `N` — message sequence number
- `FIRSTWORDLASTWORD` — first and last word of the message (letters and digits only, all caps)

**Example:** `"Hi Mike, see you tonight."` → `47:0:HITONIGHT`

---

## Author
St10539901 — Programming 1A, PROG5121



