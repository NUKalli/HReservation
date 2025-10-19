package org.example.core;

import  java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Hotel {

    private String hotelName;
    private String phoneNumber;
    private String hotelAddress;
    private List<String> amenities = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public String getHotelName() {
        return this.hotelName;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }
    public String getHotelAddress() {
        return this.hotelAddress;
    }
    public void addAmenity(String amenity) {
        this.amenities.add(amenity);
    }
    public List<String> getAmenities() {
        return this.amenities;
    }
    public void removeAmenity(int amenity) {
        this.amenities.remove(amenity);
    }
    public void addBooking(Date checkIn, Date checkOut, int roomNumber, int userID) {
        Booking booking = new Booking(checkIn, checkOut, roomNumber, userID);
        this.bookings.add(booking);
    }
    public List<Booking> getBookings() {
        return this.bookings;
    }
    public void removeBooking(int booking) {
        this.bookings.remove(booking);
    }
}
