package com.mycompany.chatapp;

/**
 * Login class for QuickChat application.
 */
public class Login {
    private String username;
    private String password;
    private boolean loggedIn = false;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Username must contain underscore AND be 5 chars or fewer.
     
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    // Password must be ≥8 chars, with at least one capital, one digit,
      //and one special character.
     
    public boolean checkPasswordComplexity() {
        if (password.length() < 8) return false;

        boolean hasCapital = false;
        boolean hasNumber  = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))      hasCapital = true;
            if (Character.isDigit(c))          hasNumber  = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public String loginUser(String inputUsername, String inputPassword) {
        if (inputUsername.equals(this.username) && inputPassword.equals(this.password)) {
            loggedIn = true;
            return "Welcome " + inputUsername + ", it is great to see you again.";
        } else {
            loggedIn = false;
            return "Username or password incorrect, please try again.";
        }
    }

    public String registerUser() {
        StringBuilder sb = new StringBuilder();

        if (checkUserName()) {
            sb.append("Username successfully captured.\n");
        } else {
            sb.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.\n");
        }

        if (checkPasswordComplexity()) {
            sb.append("Password successfully captured.");
        } else {
            sb.append("Password is not correctly formatted; please ensure that the password contains at least 8 characters, a capital letter, a number, and a special character.");
        }

        return sb.toString();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    public String getUsername() { 
        return username; 
    }
}
