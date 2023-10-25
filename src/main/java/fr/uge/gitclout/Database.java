package fr.uge.gitclout;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    static public void createDatabase() throws SQLException {
        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:ressources/db/database.db")) {
            if(conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Database.createDatabase();
    }
}
