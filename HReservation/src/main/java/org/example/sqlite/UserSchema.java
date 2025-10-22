package org.example.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSchema {
    public static void ensureUserTable(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name   TEXT NOT NULL,
                    last_name    TEXT NOT NULL,
                    phone_number TEXT,
                    email        TEXT UNIQUE NOT NULL,
                    password     TEXT NOT NULL
                    -- reservations would normally be a separate table
                    -- referencing this one via FOREIGN KEY(user_id)
                )
            """);
        }
    }
}
