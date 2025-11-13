package ec.stats.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatsDBSelect {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
    }

    public static void main(String[] args) throws Exception {
        Map arguments = parseArguments(args);

        if ("ecuser".equalsIgnoreCase((String) arguments.get("table"))) {
            selectFromEcuser((String) arguments.get("name"));
        } else if ("ecmodel".equalsIgnoreCase((String) arguments.get("table"))) {
            selectFromEcmodel((String) arguments.get("name"));
        } else {
            System.out.println("Invalid table name.");
        }
    }

    private static void selectFromEcuser(String name) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM ecuser WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Role: " + rs.getInt("role"));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private static void selectFromEcmodel(String name) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM ecmodel WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Classname: " + rs.getString("classname"));
                System.out.println("Date: " + rs.getTimestamp("date"));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private static Map parseArguments(String[] args) {
        Map arguments = new HashMap();
        for (int i = 0; i < args.length; i += 2) {
            arguments.put(args[i].substring(1), args[i + 1]);
        }
        return arguments;
    }
}
