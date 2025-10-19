package org.example.core;

import java.util.Date;

public class Booking {

    private Date checkIn;
    private Date checkOut;
    private int roomNumber;
    private int userID;

    public Booking(Date checkIn, Date checkOut, int roomNumber, int userID) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomNumber = roomNumber;
        this.userID = userID;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }
    public Date getCheckIn() {
        return this.checkIn;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }
    public Date getCheckOut() {
        return this.checkOut;
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
