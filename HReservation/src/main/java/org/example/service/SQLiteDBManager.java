package org.example.service;

import java.sql.*;

public class SQLiteDBManager {

    Connection conn = null;
    String jdbcUrl = "jdbc:sqlite:HReservation.db";

    public void deleteTable(Connection conn, String table) {
        try {
            String deleteTableSQL = "DROP TABLE " + table;
            Statement statement = conn.createStatement();
            statement.execute(deleteTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayDB(Connection conn, String tableName) throws SQLException {
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

    public Connection connect() {
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException ex) {
            System.out.println("[ERROR] Connecting to " + jdbcUrl);
        }
        return conn;
    }

    public void initialize() {
        try {
            this.conn = DriverManager.getConnection(jdbcUrl);
            try {
                String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users " +
                        "( " +
                        "userID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "firstName VARCHAR(255), " +
                        "lastName VARCHAR(255), " +
                        "phoneNumber VARCHAR(255), " +
                        "email VARCHAR(255), " +
                        "password VARCHAR(255) " +
                        "); ";
//                String createReservationTableSQL = "CREATE TABLE IF NOT EXISTS reservations " +
//                        "( " +
//                        "reservationID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        "userID INTEGER NOT NULL, " +
//                        "hotelName VARCHAR(255), " +
//                        "roomNumber VARCHAR(255), " +
//                        "bookingDate VARCHAR(255), " +
//                        "invoiceAmount REAL, " +
//                        "digitalKeyCode VARCHAR(255), " +
//                        "numberOfGuests INTEGER, " +
//                        "FOREIGN KEY (userID) REFERENCES users(userID) " +
//                        "); ";
                String createRoomTableSQL = "";
                Statement statement = conn.createStatement();
                statement.execute(createUserTableSQL);
                //statement.execute(createReservationTableSQL);

                String checkSQL = "SELECT COUNT(*) FROM users WHERE email = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                    checkStmt.setString(1, "admin@gmail.com");
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next() && rs.getInt(1) == 0) {
                        // Only insert if no admin exists
                        this.insertUser(conn, "Administrator", "Account",
                                "1-800-867-5309", "admin@gmail.com", "1234");
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void insertUser(Connection conn, String firstName, String lastName, String phoneNumber, String email, String password) throws SQLException {
        //Uses ?'s to use the PreparedStatement Object Class
        String insertSQL = "INSERT INTO users(firstName, lastName, phoneNumber, email, password) VALUES(?,?,?,?,?)";
        PreparedStatement prepStatement = conn.prepareStatement(insertSQL);
        prepStatement.setString(1, firstName);
        prepStatement.setString(2, lastName);
        prepStatement.setString(3, phoneNumber);
        prepStatement.setString(4, email);
        prepStatement.setString(5, password);
        prepStatement.executeUpdate();
    }

    public Integer verifyUser(Connection conn, String email, String password) throws SQLException {
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
}
