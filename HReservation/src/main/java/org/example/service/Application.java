package org.example.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Application {

    private int sessionID;
    private boolean authenticated = false;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Login login = new Login();
    SQLiteDBManager DB = new SQLiteDBManager();

    private boolean isAuthenticated() {
        return authenticated;
    }

    public void launch() {
        menuLogin();
        if (isAuthenticated()) {
            this.sessionID = login.getSessionID();
            System.out.println("Successfully launched HReservation as " + login.getEmail());
            menu();
        }
    }

    private void menuLogin() {
        Scanner userInput = new Scanner(System.in);
        boolean exit = false;

        while (!exit && !authenticated) {
            System.out.println("Menu:          ");
            System.out.println(" |__(l)ogin      ");
            System.out.println(" |__(n)ew User   ");
            System.out.println(" |--(q)uit       ");

            switch (userInput.nextLine()) {
                case "l": authenticated = login.start();break;
                case "n": newUserMenu();break;
                case "q": exit = true;break;
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
            DB.insertUser(firstName,lastName,phoneNumber,email,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void menu() {
        Scanner userInput = new Scanner(System.in);
        boolean quit = false;
        boolean devMode = false;

        while (!quit) {
            System.out.println("Menu:                     ");
            System.out.println(" |__(sea)rch              ");
            System.out.println(" |__(dev)eloper options   ");
            System.out.println(" |--(q)uit                ");

            switch (userInput.nextLine()) {
                case "sea": searchMenu();break;
                case "dev": devMenu(userInput);break;
                case "q": quit = true;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }
    }

    private void devMenu(Scanner userInput) {
        boolean devMode = true;
        while (devMode) {
            System.out.println("Developer Menu:           ");
            System.out.println(" |__(print) table         ");
            System.out.println(" |__(add) to table        ");
            System.out.println(" |--(e)xit                ");

            switch (userInput.nextLine()) {
                case "print": break;
                case "add": break;
                case "e": devMode = false;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }
    }

    private void searchMenu() {
        boolean searching = true;
        boolean validSearch = false;

        while (searching) {
            String checkIn = null;
            String checkOut = null;

            if (!validSearch) {
                Scanner userInput = new Scanner(System.in);
                System.out.println("Check-in: dd-MM-yyyy ");
                checkIn = userInput.nextLine();
                System.out.println("Check-out: dd-MM-yyyy");
                checkOut = userInput.nextLine();
                try {
                    LocalDate in = LocalDate.parse(checkIn, format);
                    LocalDate out = LocalDate.parse(checkOut, format);
                    if (!in.isBefore(out)) {
                        System.out.println("Check-out must be after check-in. Try again.\n");
                        continue;
                    }
                    validSearch = true;
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

            }

            else {
                try {
                    DB.availableRooms(checkIn, checkOut);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
