package org.example.service;

import java.sql.SQLException;
import java.util.Scanner;

public class WelcomeScreen {

    private boolean authenticated = false;
    private Login login = new Login();
    private String[] art = {
            "    __ __                               __             ",
            "   / // / _ \\___ ___ ___ _____  _____ _/ /_(_)__  ___",
            "  / _  / , _/ -_|_-</ -_) __/ |/ / _ `/ __/ / _ \\/ _ \\",
            " /_//_/_/|_|\\__/___/\\__/_/  |___/\\_,_/\\__/_/\\___/_//_/",
            "                                                       ",
            "      HReservation - Hotel Reservation System          ",
            "            Developed by Team Biscuit                  ",
            "    Jesse Ackley, Mahavir Kallirai, Kelvin Thomas      "
    };

        // Measure widest line
    private static int getMaxWidth(String[] art) {
        int max = 0;
        for (String line : art) {
            if (line.length() > max) {
                max = line.length();
            }
        }
        return max;
    }

    // Frame edges (rounded style)
    private static void printTop(int width) {
        System.out.println("╭" + "─".repeat(width - 2) + "╮");
    }

    private static void printBottom(int width) {
        System.out.println("╰" + "─".repeat(width - 2) + "╯");
    }

    // Print a single centered line
    private static void printLine(String text, int frameWidth, int leftPad) {
        int inner = frameWidth - 2;
        String paddedText = " ".repeat(leftPad) + text + " ".repeat(inner - text.length() - leftPad);
        System.out.println("│" + paddedText + "│");
    }

    public void start(){
        print();
        if (isAuthenticated()) {
            Application application = new Application();
            application.launch(login.getEmail(),login.getSessionID());
        }
    }

    // Print the ASCII art inside a nice rounded frame and starts the login menu
    private void print() {
        int artWidth = getMaxWidth(this.art);
        int frameWidth = artWidth + 12; // wider frame
        int leftPad = (frameWidth - 2 - artWidth) / 2;

        printTop(frameWidth);
        for (String line : this.art) {
            printLine(line, frameWidth, leftPad);
        }
        printBottom(frameWidth);
        menu();
    }

    private void menu() {
        Scanner userInput = new Scanner(System.in);
        boolean exit = false;

        while (!exit && !authenticated) {
            System.out.println("Menu:          ");
            System.out.println(" |__(L)ogin      ");
            System.out.println(" |__(N)ew User   ");
            System.out.println(" |--(Q)uit       ");

            switch (userInput.nextLine()) {
                case "L": authenticated = login.start();break;
                case "N": newUserMenu();break;
                case "Q": exit = true;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }
    }

    private void newUserMenu (){
        Scanner userInput = new Scanner(System.in);
        System.out.print("\nFirst Name: ");
        String firstName = userInput.nextLine();
        System.out.print("\nLast Name: ");
        String lastName =userInput.nextLine();
        System.out.print("\nPhone Number: ");
        String phoneNumber =userInput.nextLine();
        System.out.print("\nEmail: ");
        String email = userInput.nextLine();
        System.out.print("\nPassword: ");
        String password =userInput.nextLine();

        try {
            SQLiteDBManager DB = new SQLiteDBManager();
            DB.insertUser(DB.connect(),firstName,lastName,phoneNumber,email,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAuthenticated() {
        return authenticated;
    }

    private Login getLogin(){
        return login;
    }
}


