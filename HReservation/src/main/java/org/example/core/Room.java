package org.example.core;

public class Room {

    private int roomNumber;
    private int floorNumber;
    private int numberOfBeds;
    private double rate;
    private String roomStatus;

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getRoomNumber() {
        return this.roomNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    public int getFloorNumber() {
        return this.floorNumber;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }
    public int getNumberOfBeds() {
        return this.numberOfBeds;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
    public double getRate() {
        return this.rate;
    }
    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
    public String getRoomStatus() {
        return this.roomStatus;
    }

}
