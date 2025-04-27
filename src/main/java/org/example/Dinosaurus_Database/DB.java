package org.example.Dinosaurus_Database;

import java.sql.*;
import java.util.ArrayList;

public class DB {
    private static Connection connection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "anton", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
    public static void insert(Dino dino){
        String sql = "INSERT INTO dino (type, h, status, location) VALUES (?, ?, ?,?)";
        try (PreparedStatement stmt = connection().prepareStatement(sql)) {
            stmt.setString(1, dino.getType());
            stmt.setInt(2, dino.getH());
            stmt.setBoolean(3, dino.isStatus());
            stmt.setString(4, dino.getLocation());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<Dino> read() {
        String sql = "SELECT * FROM Dino";
        ArrayList<Dino> dinos = new ArrayList<>();
        try (Statement stmt = connection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                dinos.add(new Dino(rs.getString("type"), rs.getInt("h"), rs.getBoolean("status"), rs.getString("location")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dinos;
    }
    public static void delete(String name){
        String sql = "DELETE FROM dino WHERE type =?";
        try (PreparedStatement stmt = connection().prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateDinoById(int id, Dino dino) throws SQLException {
            String sql = "UPDATE dino SET type = ?, h = ?, status = ?, location = ?  WHERE id = ?";
            try (PreparedStatement stmt = connection().prepareStatement (sql)) {
                    stmt.setString(1, dino.getType());
                    stmt.setInt(2,dino.getH());
                    stmt.setBoolean(3,dino.isStatus());
                    stmt.setString(4, dino.getLocation());
                    stmt.setInt(5,id);
                    stmt.executeUpdate();
            }
    }
}
