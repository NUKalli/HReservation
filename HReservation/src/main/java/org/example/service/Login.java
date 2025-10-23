package org.example.service;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Provides a simple, console-based login system
 * that validates user credentials (email and password) against a database
*/
public class Login {

    /**
     * Authenticates a user with email and password
     * @return true if the user's credentials match a line in
     * the database file.
     */
    private String email;
    private int sessionID = -1;

    /**
     * Starts the login process and keeps prompting the user
     * until valid credentials are entered.
     */
    public boolean start() {
        boolean isValid = false;

        // Keep asking for credentials until login succeeds
        while (!isValid) {
            isValid = authenticate();
        }
        return isValid;
    }

    private boolean authenticate() {
        Scanner input = new Scanner(System.in);

        // Prompt for user credentials
        System.out.println("Enter email: ");
        email = input.nextLine();

        System.out.println("Enter password: ");
        String password = input.nextLine();

        //Checks the database for account matching user input. Returns a session ID if found.
        SQLiteDBManager DB = new SQLiteDBManager();
        DB.initialize();

        try {
            sessionID = DB.verifyUser(email, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (sessionID == -1) {
            System.out.println("Invalid email or password...");
            return false;
        }
        else {
            return true;
        }
    }

    public String getEmail(){
        return email;
    }

    public int getSessionID(){
        return sessionID;
    }
}

