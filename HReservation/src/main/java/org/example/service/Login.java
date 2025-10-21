package org.example.service;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Provides a simple, console-based login system
 * that validates user credentials (email and password) against a database stored
 * in a local text file named users_db located in src/main/resources/
 * Each line of the file should be formatted:
 *     email,password
 * Example:
 *     admin@gmail.com,1234
 *     user@gmail.com,1234
 */
public class Login {

    /**
     * Authenticates a user with email and password by
     * checking users_db file
     * @return true if the user's credentials match a line in
     * the database file.
     */
    private boolean authenticate() {
        Scanner input = new Scanner(System.in);

        // Prompt for user credentials
        System.out.println("Enter email: ");
        String email = input.nextLine();

        System.out.println("Enter password: ");
        String password = input.nextLine();

        // Combine input into the format used in users_db
        String userInput = email + "," + password;

        try {
            // Load users_db file from resources
            InputStream database = getClass().getClassLoader().getResourceAsStream("users_db");

            // If file not found, return false
            if (database == null) {
                System.out.println("[ERROR] - Failed to open users_db file.");
                return false;
            }

            // Read and compare each line with user input
            Scanner openDB = new Scanner(database);
            while (openDB.hasNextLine()) {
                String line = openDB.nextLine();
                if (userInput.equals(line)) {
                    return true; // Successful login
                }
            }

            // No match found
            System.out.println("Invalid email or password...");
        } catch (Exception e) {
            // Catch unexpected errors
            System.out.println("[ERROR] - Unknown error during login.");
        }

        // Return false if login failed
        return false;
    }

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
}

