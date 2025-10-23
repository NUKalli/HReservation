package org.example.service;

import java.sql.*;

public class SQLiteDBManager {

    Connection conn = null;
    String jdbcUrl = "jdbc:sqlite:HReservation.db";

    public void deleteTable(String table) {
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);

            String deleteTableSQL = "DROP TABLE " + table;
            Statement statement = conn.createStatement();
            statement.execute(deleteTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayDB(String tableName) throws SQLException {
        this.conn = DriverManager.getConnection(jdbcUrl);

        String selectSQL = "SELECT * from " + tableName;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQL);

        System.out.println();
        System.out.println("----- " + tableName + " -----");
        while (resultSet.next()) {
            System.out.print("User: " + resultSet.getString("userID") + ", ");
            System.out.print(resultSet.getString("firstName") + ", ");
            System.out.print(resultSet.getString("lastName") + ", ");
            System.out.print(resultSet.getString("phoneNumber") + ", ");
            System.out.print(resultSet.getString("email") + ", ");
            System.out.print(resultSet.getString("password") + "\n");
        }
        System.out.println("--------------------");
    }

    public void initialize() {
        int ROOMS = 20;
        int FLOORS = 1;
        double RATE = 120.00;
        String AVAILABLE = "Available";

        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            Statement statement = conn.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("PRAGMA busy_timeout = 5000;");
            try {
                //This section initializes SQLite database with tables if they do not already exist
                String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users " +
                        "( " +
                        "userID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "firstName VARCHAR(255), " +
                        "lastName VARCHAR(255), " +
                        "phoneNumber VARCHAR(255), " +
                        "email VARCHAR(255), " +
                        "password VARCHAR(255) " +
                        "); ";
                String createReservationTableSQL = "CREATE TABLE IF NOT EXISTS reservations " +
                        "( " +
                        "reservationID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "userID INTEGER NOT NULL, " +
                        "hotelName VARCHAR(255), " +
                        "roomNumber VARCHAR(255), " +
                        "bookingDate VARCHAR(255), " +
                        "invoiceAmount REAL, " +
                        "digitalKeyCode VARCHAR(255), " +
                        "numberOfGuests INTEGER, " +
                        "FOREIGN KEY (userID) REFERENCES users(userID) " +
                        "); ";
                String createRoomTableSQL = "CREATE TABLE IF NOT EXISTS rooms " +
                        "( " +
                        "roomID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "reservationID INTEGER, " +
                        "floorNumber INTEGER, " +
                        "rate REAL, " +
                        "roomStatus VARCHAR(255) " +
                        "); ";
                String createHotelTableSQL = "CREATE TABLE IF NOT EXISTS hotels " +
                        "( " +
                        "hotelID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "phoneNumber VARCHAR(255), " +
                        "hotelAddress VARCHAR(255) " +
                        "); ";
                String createBookingTableSQL = "CREATE TABLE IF NOT EXISTS bookings " +
                        "( " +
                        "bookingID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "reservationID INTEGER NOT NULL, " +
                        "hotelID INTEGER NOT NULL, " +
                        "checkIn VARCHAR(255), " +
                        "checkOut VARCHAR(255), " +
                        "roomNumber INTEGER, " +
                        "userID INTEGER, " +
                        "FOREIGN KEY (reservationID) REFERENCES reservations(reservationID), " +
                        "FOREIGN KEY (userID) REFERENCES users(userID), " +
                        "FOREIGN KEY (hotelID) REFERENCES hotels(hotelID) " +
                        "); ";
                statement.execute(createUserTableSQL);
                statement.execute(createReservationTableSQL);
                statement.execute(createRoomTableSQL);
                statement.execute(createHotelTableSQL);
                statement.execute(createBookingTableSQL);

                //This section checks if the default admin account exists. Creates one if it does not exist.
                boolean needAdminInsert = false;
                String checkAdminUser = "SELECT COUNT(*) FROM users WHERE email = ?";

                try (PreparedStatement checkStmt = conn.prepareStatement(checkAdminUser)) {
                    checkStmt.setString(1, "admin@gmail.com");
                    ResultSet rs = checkStmt.executeQuery();
                    needAdminInsert = rs.next() && rs.getInt(1) == 0;
                }
                if (needAdminInsert) {
                    // Only insert if no admin exists
                    this.insertUser("Administrator", "Account",
                            "1-800-867-5309", "admin@gmail.com", "1234");
                }

                //Initializes the table with rooms if not already done
                boolean needRoomInsert = false;
                String checkRoomsInit = "SELECT COUNT(*) AS total FROM rooms";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkRoomsInit)) {
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        int count = rs.getInt("total");
                        if (count == 0) {
                            needRoomInsert = true;
                        }
                    }
                }
                if (needRoomInsert){
                    for (int room = 1; room <= ROOMS; room++) {
                        for (int floor = 1; floor <= FLOORS; floor++){
                            insertRoom(floor, RATE, AVAILABLE);
                        }
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void insertUser(String firstName, String lastName, String phoneNumber, String email, String password) throws SQLException {
        //Uses ?'s to use the PreparedStatement Object Class
        this.conn = DriverManager.getConnection(jdbcUrl);
        String insertSQL = "INSERT INTO users(firstName, lastName, phoneNumber, email, password) VALUES(?,?,?,?,?)";

        PreparedStatement prepStatement = conn.prepareStatement(insertSQL);
        prepStatement.setString(1, firstName);
        prepStatement.setString(2, lastName);
        prepStatement.setString(3, phoneNumber);
        prepStatement.setString(4, email);
        prepStatement.setString(5, password);
        prepStatement.executeUpdate();
    }

    public Integer verifyUser(String email, String password) throws SQLException {
        this.conn = DriverManager.getConnection(jdbcUrl);
        String sql = "SELECT userID FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userID"); // return the matching user's ID
                } else {
                    return -1; // no match found
                }
            }
        }
    }

    public void insertRoom(int reservationID, int floorNumber, double rate, String roomStatus){
        String insertRoom = "INSERT INTO rooms (reservationID, floorNumber, rate, roomStatus) VALUES (?, ?, ?, ?)";
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertRoom(int floorNumber, double rate, String roomStatus){
        String insertRoom = "INSERT INTO rooms (floorNumber, rate, roomStatus) VALUES (?, ?, ?)";
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void availableRooms(){

    }
}
