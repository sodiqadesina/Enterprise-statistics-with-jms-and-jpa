package ec.stats.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatsDBCreate {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
    }

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();

            // Creating ecuser table
            String createEcuser = "CREATE TABLE IF NOT EXISTS ecuser ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(50) NOT NULL, "
                + "password VARCHAR(64) NOT NULL, "
                + "role INT NOT NULL)";
            stmt = conn.prepareStatement(createEcuser);
            stmt.executeUpdate();

            // Creatin ecmodel table
            String createEcmodel = "CREATE TABLE IF NOT EXISTS ecmodel ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(50) NOT NULL, "
                + "object MEDIUMBLOB NOT NULL, "
                + "classname VARCHAR(255) NOT NULL, "
                + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt = conn.prepareStatement(createEcmodel);
            stmt.executeUpdate();

            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
