package org.example.core;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int userID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public int getUserID() {
        return this.userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName() {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservations() {
        return this.reservations;
    }

    //public Reservation makeReservation() {}
    public void modifyReservation(Reservation reservation) {}
    public void cancelReservation(Reservation reservation) {}
    //public boolean login(String email, String password) {}
    public void logout() {}
}
