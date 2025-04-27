package org.example;
import java.sql.*;

public  class database {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    public static final String user = "anton";
    public static final String password = "";
    public static void main(String[] args) {

        conn();
        remove(16);


    }

    public static void conn(){

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cars ");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(String name) {

        String sql = "INSERT INTO cars (name) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

            System.out.println("✅ Добавлено в базу: " + name);

        } catch (Exception e) {
            System.out.println("❌ Ошибка при вставке: " + e.getMessage());
        }
    }
public static void remove(int id){
    String sql = "REMOVE from cars id(id)";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        
        stmt.executeUpdate();

        System.out.println("✅ Добавлено в базу: " + id);

    } catch (Exception e) {
        System.out.println("❌ Ошибка при вставке: " + e.getMessage());
    }
}

}
