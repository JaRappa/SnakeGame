package com.example.snake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost/snakegame";
    private static final String DATABASE_USER = "scott";
    private static final String DATABASE_PASSWORD = "tiger";
    // Establishes a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    // Saves a score to the database
    public static void saveScore(int score) {
        String sql = "INSERT INTO Scores (Score) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieves the top scores from the database
    public static List<Integer> getTopScores(int limit) {
        List<Integer> scores = new ArrayList<>();
        String sql = "SELECT Score FROM Scores ORDER BY Score DESC LIMIT ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                scores.add(rs.getInt("Score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
