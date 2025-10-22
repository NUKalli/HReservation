package org.example.service;

import java.sql.SQLException;
import java.util.Scanner;

public class Application {
//    private void addReservation() {
//        SQLiteDBManager DB =new SQLiteDBManager();
//    }
    private int sessionID;

    public void launch(String email, int sessionID) {
        System.out.println("Successfully launched HReservation as " + email);
        this.sessionID = sessionID;
        menu();
    }

    private void menu() {
        Scanner userInput = new Scanner(System.in);
        boolean exit = false;
        boolean devMode = false;

        while (!exit) {
            System.out.println("Menu:                     ");
            System.out.println(" |__(sea)rch              ");
            System.out.println(" |__(dev)eloper options   ");
            System.out.println(" |--(q)uit                ");

            switch (userInput.nextLine()) {
                case "sea": break;
                case "dev": break;
                case "q": exit = true;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }

        while (!devMode) {
            System.out.println("Developer Menu:           ");
            System.out.println(" |__(print) table         ");
            System.out.println(" |__(add) to table        ");
            System.out.println(" |--(q)uit                ");

            switch (userInput.nextLine()) {
                case "print": break;
                case "add": break;
                case "q": exit = true;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }

    }
}
