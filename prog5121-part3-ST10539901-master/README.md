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
Student — Programming 1A, PROG5121/p/w


## YouTube

https://youtu.be/72QVjEv5N2M?si=pYZ7RtN2pTqg7RUA



## ScreenShots


New Screenshot 

<img width="1362" height="1017" alt="image" src="https://github.com/user-attachments/assets/de308ff9-be56-4e42-a561-b2c0b153e58c" />

<img width="1036" height="797" alt="image" src="https://github.com/user-attachments/assets/00784569-4b5c-4896-b23e-bcb482717111" />


<img width="1332" height="867" alt="image" src="https://github.com/user-attachments/assets/ebd69830-0ce8-4c76-b312-705894771637" />

<img width="1337" height="900" alt="image" src="https://github.com/user-attachments/assets/5252f9e2-c67b-4c23-b90d-1b631f50f492" />
<img width="1347" height="977" alt="image" src="https://github.com/user-attachments/assets/49404bfe-6105-4470-b49c-7781994ed616" />


--------

Old Screenshot

**These screenshot show that the previous part 1 and 2 still functions properly**

<img width="811" height="900" alt="image" src="https://github.com/user-attachments/assets/9307f51d-dbc5-40d3-8e9a-ea6ef0c6dc82" />

<img width="1004" height="931" alt="image" src="https://github.com/user-attachments/assets/e869998c-87cd-494e-b1ae-d2d038db2b56" />
<img width="1432" height="1003" alt="image" src="https://github.com/user-attachments/assets/8e496e03-16c3-4402-9bfa-67c9725b4a0b" />
<img width="960" height="966" alt="image" src="https://github.com/user-attachments/assets/1db3e06e-514f-478a-a1c4-d3022c8eb73c" />
<img width="1148" height="938" alt="image" src="https://github.com/user-attachments/assets/3a69dc0f-7e50-4a07-b078-0af1b9999062" />
<img width="1040" height="952" alt="image" src="https://github.com/user-attachments/assets/70b01a32-cc72-49af-bde0-a986061527b4" />
<img width="1858" height="931" alt="image" src="https://github.com/user-attachments/assets/0e9f8ced-d21f-4f5f-a908-17e8a6f42598" />
<img width="1861" height="937" alt="image" src="https://github.com/user-attachments/assets/6748d2d1-e416-4654-8d1a-61bb57d80e36" />


