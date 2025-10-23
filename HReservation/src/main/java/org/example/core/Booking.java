package org.example.core;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Booking {

    private LocalDate checkIn;
    private LocalDate checkOut;
    private int roomNumber;
    private int userID;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Booking(LocalDate checkIn, LocalDate checkOut, int roomNumber, int userID) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomNumber = roomNumber;
        this.userID = userID;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }
    public LocalDate getCheckIn() {
        return this.checkIn;
    }
    public void setCheckIn(String checkIn) {
        this.checkIn = LocalDate.parse(checkIn, format);
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
    public LocalDate getCheckOut() {
        return this.checkOut;
    }
    public void setCheckOut(String checkOut) {
        this.checkOut = LocalDate.parse(checkOut, format);
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getRoomNumber() {
        return this.roomNumber;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return this.userID;
    }


}
