package org.example.service;

import java.sql.*;

public class SQLiteDBManager {
    private String AVAILABLE = "Available";
    private String BOOKED = "Booked";
    private int HOTEL1 = 1;
    private Connection conn = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String jdbcUrl = "jdbc:sqlite:HReservation.db";

    public void initialize() {
        int ROOMS = 20;
        int FLOORS = 1;
        double RATE = 120.00;

        //This section initializes SQLite database with tables if they do not already exist
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
            Statement statement = conn.createStatement()){

            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("PRAGMA busy_timeout = 5000;");
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users " +
                    "( " +
                    "userID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "phoneNumber TEXT, " +
                    "email TEXT UNIQUE, " +
                    "password TEXT " +
                    "); ";
            String createHotelTableSQL = "CREATE TABLE IF NOT EXISTS hotels " +
                    "( " +
                    "hotelID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "hotelName TEXT, " +
                    "phoneNumber TEXT, " +
                    "hotelAddress TEXT " +
                    "); ";
            String createRoomTableSQL = "CREATE TABLE IF NOT EXISTS rooms " +
                    "( " +
                    "roomID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "hotelID INTEGER NOT NULL, " +
                    "floorNumber INTEGER, " +
                    "rate REAL, " +
                    "roomStatus TEXT, " +
                    "FOREIGN KEY (hotelID) REFERENCES hotels(hotelID) ON DELETE CASCADE, " +
                    "UNIQUE (hotelID, roomID) " +
                    "); ";
            String createReservationTableSQL = "CREATE TABLE IF NOT EXISTS reservations " +
                    "( " +
                    "reservationID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userID INTEGER NOT NULL, " +
                    "hotelID INTEGER NOT NULL, " +
                    "numberOfGuests INTEGER, " +
                    "FOREIGN KEY (userID) REFERENCES users(userID), " +
                    "FOREIGN KEY (hotelID) REFERENCES hotels(hotelID) " +
                    "); ";
            String createBookingTableSQL = "CREATE TABLE IF NOT EXISTS bookings " +
                    "( " +
                    "bookingID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "reservationID INTEGER NOT NULL, " +
                    "roomID INTEGER NOT NULL, " +
                    "checkIn TEXT, " +
                    "checkOut TEXT, " +
                    "FOREIGN KEY (reservationID) REFERENCES reservations(reservationID), " +
                    "FOREIGN KEY (roomID) REFERENCES rooms(roomID) " +
                    "); ";
            statement.execute(createUserTableSQL);
            statement.execute(createHotelTableSQL);
            statement.execute(createRoomTableSQL);
            statement.execute(createReservationTableSQL);
            statement.execute(createBookingTableSQL);

        } catch (SQLException e) {
            System.out.println("[CREATE TABLES] " + e.getMessage());
        }

        //------------------------------------------------------------------------------------------
        //This section checks if the default admin account exists. Creates one if it does not exist.
        //------------------------------------------------------------------------------------------
        boolean needAdminInsert = false;
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            String checkAdminUser = "SELECT COUNT(*) FROM users WHERE email = ?";
            this.preparedStatement = this.conn.prepareStatement(checkAdminUser);
            this.preparedStatement.setString(1, "admin@gmail.com");
            this.resultSet = this.preparedStatement.executeQuery();
            needAdminInsert = this.resultSet.next() && this.resultSet.getInt(1) == 0;
        } catch (SQLException e) {
            System.out.println("[DEFAULT ADMIN]" + e.getMessage());
        } finally {
            try {
                this.resultSet.close();
            } catch (SQLException e) {
                System.out.println("[DEFAULT ADMIN]" + e.getMessage());
            }
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("[DEFAULT ADMIN]" + e.getMessage());
            }
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println("[DEFAULT ADMIN]" + e.getMessage());
            }
        }
        if (needAdminInsert) {
            // Only insert if no admin exists
            this.insertUser("Administrator", "Account",
                    "1-800-867-5309", "admin@gmail.com", "1234");
        }

        //------------------------------------------------------------------------------------------
        //This section checks if the default hotel exists. Creates it if it does not exist.
        //------------------------------------------------------------------------------------------
        boolean needHotelInsert = false;
        String checkHotelTable = "SELECT hotelID FROM hotels WHERE hotelID = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
            PreparedStatement preparedStatement = conn.prepareStatement(checkHotelTable);){
            preparedStatement.setInt(1, HOTEL1);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                needHotelInsert = !resultSet.next();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("[DEFAULT HOTEL] " + e.getMessage());
        }
        if (needHotelInsert) {
            insertHotel("GrandHotel Pupp", "+420 353 109 631", "123 Street");
        }

        //-----------------------------------------------------------
        //Checks if the rooms table needs to be initialized
        //-----------------------------------------------------------
        boolean needRoomInsert = false;
        String checkRoomsInit = "SELECT COUNT(*) AS total FROM rooms";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
            PreparedStatement preparedStatement = conn.prepareStatement(checkRoomsInit);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int count = resultSet.getInt("total");
                if (count == 0) {
                    needRoomInsert = true;
                }
            }
        } catch (SQLException e){
            System.out.println("[PRE INIT ROOMS] " + e.getMessage());
        }

        //-----------------------------------------------------------
        //Initializes the rooms table with rooms if not already done
        //-----------------------------------------------------------
        try {
            if (needRoomInsert){
                for (int room = 1; room <= ROOMS; room++) {
                    for (int floor = 1; floor <= FLOORS; floor++){
                        insertRoom(HOTEL1, floor, RATE, AVAILABLE);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[INIT ROOMS] " + e.getMessage());;
        }
    }

    //----------------------------------------------------
    // ---------------- METHODS FOR USERS ----------------
    //----------------------------------------------------
    public void insertUser(String firstName, String lastName, String phoneNumber, String email, String password) {
        String insertSQL = "INSERT INTO users(firstName, lastName, phoneNumber, email, password) VALUES(?,?,?,?,?)";
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            this.preparedStatement = this.conn.prepareStatement(insertSQL);
            this.preparedStatement.setString(1, firstName);
            this.preparedStatement.setString(2, lastName);
            this.preparedStatement.setString(3, phoneNumber);
            this.preparedStatement.setString(4, email);
            this.preparedStatement.setString(5, password);
            this.preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[INSERT USER]" + e.getMessage());
        } finally {
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("[INSERT USER]" + e.getMessage());
            }
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println("[INSERT USER]" + e.getMessage());
            }
        }
    }

    public Integer verifyUser(String email, String password) {
        String sql = "SELECT userID FROM users WHERE email = ? AND password = ?";
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            this.preparedStatement = this.conn.prepareStatement(sql);
            this.preparedStatement.setString(1, email);
            this.preparedStatement.setString(2, password);
            this.resultSet = this.preparedStatement.executeQuery();

            if (this.resultSet.next()) {
                return this.resultSet.getInt("userID"); // return the matching user's ID
            } else {
                return -1; // no match found
            }

        } catch (SQLException e) {
            System.out.println("[VERIFY USER]" + e.getMessage());
        } finally {
            try {
                this.resultSet.close();
            } catch (SQLException e) {
                System.out.println("[VERIFY USER]" + e.getMessage());
            }
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("[VERIFY USER]" + e.getMessage());
            }
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println("[VERIFY USER]" + e.getMessage());
            }
        }
        return -1;
    }

    public void printUsers() {
        String sql = "SELECT * FROM users";
        System.out.println("\n=== USERS TABLE ===");

        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            this.preparedStatement = this.conn.prepareStatement(sql);
            this.resultSet = this.preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                int id = this.resultSet.getInt("userID");
                String first = this.resultSet.getString("firstName");
                String last = this.resultSet.getString("lastName");
                String phone = this.resultSet.getString("phoneNumber");
                String email = this.resultSet.getString("email");
                String password = this.resultSet.getString("password");

                System.out.printf("ID: %d | %s %s | Phone: %s | Email: %s | Password: %s%n",
                        id, first, last, phone, email, password);
            }

        } catch (SQLException e) {
            System.out.println("[PRINT USERS]" + e.getMessage());
        } finally {
            try {
                this.resultSet.close();
            } catch (SQLException e) {
                System.out.println("[PRINT USERS]" + e.getMessage());
            }
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("[PRINT USERS]" + e.getMessage());
            }
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println("[PRINT USERS]" + e.getMessage());
            }
        }
    }
    //----------------------------------------------------
    // ---------------- METHODS FOR ROOMS ----------------
    //----------------------------------------------------
    public void bookRoom(int roomID, int userID) {
        boolean roomVerified = false;
        String validRoom = "SELECT roomID, roomStatus FROM rooms WHERE roomID = ?";
        try(Connection conn = DriverManager.getConnection(jdbcUrl);
            PreparedStatement preparedStatement = conn.prepareStatement(validRoom)){

            preparedStatement.setInt(1, roomID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("roomId") == roomID && resultSet.getString("roomStatus").equals(AVAILABLE)){
                    roomVerified = true;
                }
                else {
                    System.out.println("Room " + roomID + " is unavailable.");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (roomVerified) {
                updateRoomStatus(roomID, BOOKED);
            }
        } catch (Exception e) {
            System.out.println("[BOOK ROOM] " + e.getMessage());
        }
    }
//
//    public void insertRoom(int reservationID, int floorNumber, double rate, String roomStatus){
//        String insertRoom = "INSERT INTO rooms (reservationID, floorNumber, rate, roomStatus) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DriverManager.getConnection(jdbcUrl);){
//
//        } catch (SQLException e) {
//            System.out.println("[INSERT ROOMS]" + e.getMessage());
//        }
//    }

    public void insertRoom(int hotelID, int floorNumber, double rate, String roomStatus){
        String insertRoom = "INSERT INTO rooms (hotelID, floorNumber, rate, roomStatus) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
            PreparedStatement preparedStatement = conn.prepareStatement(insertRoom);
            Statement statement = conn.createStatement()){
            statement.execute("PRAGMA foreign_keys = ON;");
            preparedStatement.setInt(1, hotelID);
            preparedStatement.setInt(2, floorNumber);
            preparedStatement.setDouble(3, rate);
            preparedStatement.setString(4, roomStatus);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("[INSERT ROOMS]" + e.getMessage());
        }
    }

    public void availableRooms(String checkIn, String checkOut){
        String sql = "SELECT roomID, floorNumber, rate FROM rooms WHERE roomStatus = ?";
        boolean roomsAvailable = false;
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            //this.statement = this.conn.createStatement();
            this.preparedStatement = this.conn.prepareStatement(sql);
            this.preparedStatement.setString(1, AVAILABLE);
            this.resultSet = this.preparedStatement.executeQuery();

            System.out.println("Available rooms:");
            while (this.resultSet.next()) {
                int roomID = this.resultSet.getInt("roomID");
                int floorNumber = this.resultSet.getInt("floorNumber");
                double rate = this.resultSet.getDouble("rate");
                System.out.println("\t|-- Room#: " + roomID + "\tFloor: " + floorNumber + "\t$" + rate + "/night");
                roomsAvailable = true;
            }
            if (!roomsAvailable) {
                System.out.println("No rooms available from " + checkIn + " - " + checkOut + ".");
            }
        } catch (SQLException e) {
            System.out.println("[AVAILABLE ROOMS]" + e.getMessage());
        } finally {
            try {
                this.resultSet.close();
            } catch (SQLException e) {
                System.out.println("[AVAILABLE ROOMS]" + e.getMessage());
            }
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("[AVAILABLE ROOMS] " + e.getMessage());
            }
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println("[AVAILABLE ROOMS]" + e.getMessage());
            }
        }
    }

    public void updateRoomStatus(int roomID, String newStatus) {
        String sql = "UPDATE rooms SET roomStatus = ? WHERE roomID = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            statement.execute("PRAGMA foreign_keys = ON;");
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, roomID);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                System.out.println("No room found with ID: " + roomID);
            }

        } catch (SQLException e) {
            System.out.println("[UPDATE ROOM STATUS] " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void printRooms() {
        final String sql = "SELECT roomID, hotelID, floorNumber, rate, roomStatus FROM rooms";

        System.out.println("\n=== ROOMS TABLE ===");

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("PRAGMA busy_timeout = 5000;");

            boolean hasResults = false;
            while (resultSet.next()) {
                int roomID = resultSet.getInt("roomID");
                int hotelID = resultSet.getInt("hotelID");
                int floorNumber = resultSet.getInt("floorNumber");
                double rate = resultSet.getDouble("rate");
                String status = resultSet.getString("roomStatus");

                System.out.printf("RoomID: %d | HotelID: %d | Floor: %d | Rate: %.2f | Status: %s%n",
                        roomID, hotelID, floorNumber, rate, status);
                hasResults = true;
            }

            if (!hasResults) {
                System.out.println("No rooms found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("[PRINT ROOMS] " + e.getMessage());
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------
    // ---------------- METHODS FOR BOOKINGS ----------------
    //-------------------------------------------------------
    public void insertBooking(int reservationID, int roomID, String checkIn, String checkOut) {
        String sql = "INSERT INTO bookings (reservationID, roomID, checkIn, checkOut) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            statement.execute("PRAGMA foreign_keys = ON;");
            preparedStatement.setInt(1, reservationID);
            preparedStatement.setInt(2, roomID);
            preparedStatement.setString(3, checkIn);
            preparedStatement.setString(4, checkOut);
            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                System.out.println("[INSERT BOOKING] No rows inserted.");
            }
        } catch (SQLException e) {
            System.out.println("[INSERT BOOKING] " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printBookings() {
        final String sql = "SELECT bookingID, reservationID, roomID, checkIn, checkOut FROM bookings";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement prepareStatement = conn.prepareStatement(sql);
             ResultSet resulSet = prepareStatement.executeQuery()) {

            statement.execute("PRAGMA foreign_keys = ON;");

            System.out.println("\n==== BOOKINGS TABLE ====");
            boolean hasResults = false;

            while (resulSet.next()) {
                hasResults = true;
                int bookingID = resulSet.getInt("bookingID");
                int reservationID = resulSet.getInt("reservationID");
                int roomID = resulSet.getInt("roomID");
                String checkIn = resulSet.getString("checkIn");
                String checkOut = resulSet.getString("checkOut");

                System.out.printf("Booking ID: %d | Reservation ID: %d | Room ID: %d | Check-In: %s | Check-Out: %s%n",
                        bookingID, reservationID, roomID, checkIn, checkOut);
            }

            if (!hasResults) {
                System.out.println("No bookings found.");
            }

        } catch (SQLException e) {
            System.out.println("[PRINT BOOKINGS] " + e.getMessage());
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------
    // ---------------- METHODS FOR RESERVATIONS ----------------
    //-----------------------------------------------------------
    public int insertReservation(int userID, int hotelID, int numberOfGuests) {
        int generatedID = -1;
        String sql = "INSERT INTO reservations (userID, hotelID, numberOfGuests) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement prepareStatement = conn.prepareStatement(sql)) {

            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("PRAGMA busy_timeout = 5000;");
            prepareStatement.setInt(1, userID);
            prepareStatement.setInt(2, hotelID);
            prepareStatement.setInt(3, numberOfGuests);
            int rows = prepareStatement.executeUpdate(); // Perform the INSERT

            if (rows > 0) {
                System.out.println("Reservation for " + numberOfGuests + " people completed!");
                try (ResultSet keys = prepareStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedID = keys.getInt(1);
                    }
                }
            }
            else{
                System.out.println("[INSERT RESERVATION] No rows inserted.");
            }
        } catch (SQLException e) {
            System.out.println("[INSERT RESERVATION] " + e.getMessage());
            e.printStackTrace();
        }
        return generatedID;
    }


    public void printReservations() {
        final String sql = "SELECT reservationID, userID, hotelID, numberOfGuests FROM reservations";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            statement.execute("PRAGMA foreign_keys = ON;");

            System.out.println("==== Reservations ====");
            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                int reservationID = resultSet.getInt("reservationID");
                int userID = resultSet.getInt("userID");
                int hotelID = resultSet.getInt("hotelID");
                int guests = resultSet.getInt("numberOfGuests");

                System.out.printf("Reservation ID: %d | User ID: %d | Hotel ID: %d | Guests: %d%n",
                        reservationID, userID, hotelID, guests);
            }

            if (!hasResults) {
                System.out.println("No reservations found.");
            }

        } catch (SQLException e) {
            System.out.println("[PRINT RESERVATIONS] " + e.getMessage());
            e.printStackTrace();
        }
    }


    //----------------------------------------------------
    // ---------------- METHODS FOR HOTEL ----------------
    //----------------------------------------------------
    public void insertHotel(String hotelName, String phoneNumber, String hotelAddress) {
        final String sql = "INSERT INTO hotels (hotelName, phoneNumber, hotelAddress) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement statement = conn.createStatement();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            statement.execute("PRAGMA foreign_keys = ON;");
            preparedStatement.setString(1, hotelName);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, hotelAddress);
            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                System.out.println("[INSERT HOTEL] No hotel added");
            }
        } catch (SQLException e) {
            System.out.println("[INSERT HOTEL] " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printHotels() {
        String sql = "SELECT hotelID, hotelName, phoneNumber, hotelAddress FROM hotels";
        System.out.println("\n=== HOTELS TABLE ===");

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                int id = rs.getInt("hotelID");
                String name = rs.getString("hotelName");
                String phone = rs.getString("phoneNumber");
                String address = rs.getString("hotelAddress");

                System.out.printf("HotelID: %d | Name: %s | Phone: %s | Address: %s%n",
                        id, name, phone, address);
            }

            if (!hasRows) {
                System.out.println("No hotels found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("[PRINT HOTELS] " + e.getMessage());
        }
    }


}
