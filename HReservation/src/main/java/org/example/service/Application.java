package org.example.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Application {

    private int sessionID;
    private boolean authenticated = false;
    private String lastCheckIn = null;
    private String lastCheckOut = null;
    private int lastNumberOfGuest = -1;
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
            System.out.println("Successfully launched HReservation as " + login.getEmail() + "\nSessionID: " + sessionID);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void menu() {
        Scanner userInput = new Scanner(System.in);
        boolean quit = false;
        boolean devMode = false;

        while (!quit) {
            System.out.println("Menu:                     ");
            System.out.println(" |__(sea)rch              ");
            System.out.println(" |__(boo)k a room         ");
            System.out.println(" |__(dev)eloper options   ");
            System.out.println(" |--(q)uit                ");

            switch (userInput.nextLine()) {
                case "sea": searchMenu();break;
                case "boo": bookingMenu();break;
                case "dev": devMenu(userInput);break;
                case "q": quit = true;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }
    }

    private void devMenu(Scanner userInput) {
        boolean devMode = true;
        while (devMode) {
            System.out.println("Developer Menu:                 ");
            System.out.println(" |__(print users) table         ");
            System.out.println(" |__(print hotels) table        ");
            System.out.println(" |__(print rooms) table         ");
            System.out.println(" |__(print res)ervations        ");
            System.out.println(" |--(print boo)kings            ");
            System.out.println(" |__(add) to table              ");
            System.out.println(" |--(e)xit                      ");

            switch (userInput.nextLine()) {
                case "print users": DB.printUsers();break;
                case "print rooms": DB.printRooms();break;
                case "print hotels": DB.printHotels();break;
                case "print res": DB.printReservations();break;
                case "print boo": DB.printBookings();break;
                case "add": break;
                case "e": devMode = false;break;
                default: System.out.println("[ERROR] Invalid option.");break;
            }
        }
    }

    private void searchMenu() {
        boolean searching = true;
        boolean validSearch = false;
        lastNumberOfGuest = -1;

        while (searching) {
            String numberOfGuests = null;

            if (!validSearch) {
                lastCheckIn = null;
                lastCheckOut = null;
                Scanner userInput = new Scanner(System.in);
                System.out.println("How many guests? ");
                numberOfGuests = userInput.nextLine();
                System.out.println("Check-in: dd-MM-yyyy ");
                lastCheckIn = userInput.nextLine();
                System.out.println("Check-out: dd-MM-yyyy");
                lastCheckOut = userInput.nextLine();
                try {
                    lastNumberOfGuest = Integer.parseInt(numberOfGuests);
                } catch (Exception e) {
                    System.out.println("Please enter a valid number of guests.");
                    continue;
                }
                try {
                    LocalDate in = LocalDate.parse(lastCheckIn, format);
                    LocalDate out = LocalDate.parse(lastCheckOut, format);
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
                    DB.availableRooms(lastCheckIn, lastCheckOut);
                    searching = false;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void bookingMenu() {
        boolean dateConfirmed = false;
        Scanner userInput = new Scanner(System.in);
        while (!dateConfirmed) {
            if (lastCheckIn == null || lastCheckOut == null || lastNumberOfGuest == -1){
                searchMenu();
                continue;
            }
            System.out.println("Book room number for " + lastNumberOfGuest + " people from " + lastCheckIn + " to " + lastCheckOut + ":\nY/N?");
            switch (userInput.nextLine()) {
                case "Y":
                    dateConfirmed = true;
                    break;
                case "N":
                    searchMenu();
                    break;
                default:
                    System.out.println("Please enter Y/N:");
                    break;
            }
        }
        try {
            System.out.println("Choose your room number: ");
            String intCheck = userInput.nextLine();
            int roomNumber = Integer.parseInt(intCheck);
            DB.insertBooking(DB.insertReservation(sessionID, 1, lastNumberOfGuest), roomNumber,lastCheckIn,lastCheckOut);
            DB.bookRoom(roomNumber, sessionID);
            ;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid Room Number " + e.getMessage());
        }
    }


}
