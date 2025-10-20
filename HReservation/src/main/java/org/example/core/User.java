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

    //public Reservation makeReservation() {}
    public void modifyReservation(Reservation reservation) {}
    public void cancelReservation(Reservation reservation) {}
    //public boolean login(String email, String password) {}
    public void logout() {}
}
