package controller;

import java.sql.*;
import model.DAO.*;
import java.time.LocalDate;

public class StreakService {

    public static int updateStreak(int userId) throws SQLException {
        Connection conn = Database.getConnection(); 
        String query = "SELECT Last_Activity, Streak_Count FROM user_streaks WHERE User_Id = ?";

        LocalDate today = LocalDate.now();
        int streakCount = 0;

        try (
            PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    LocalDate lastActivity = rs.getDate("Last_Activity").toLocalDate();
                    streakCount = rs.getInt("Streak_Count");

                    if (lastActivity.equals(today.minusDays(1))) {
                        streakCount++;
                    } else if (!lastActivity.equals(today)) {
                        streakCount = 1;
                    }

                    String updateQuery = "UPDATE user_streaks SET Last_Activity = ?, Streak_Count = ? WHERE User_Id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setDate(1, Date.valueOf(today));
                        updateStmt.setInt(2, streakCount);
                        updateStmt.setInt(3, userId);
                        updateStmt.executeUpdate();
                    }
                }else {
                    String insertQuery = "INSERT INTO user_streaks (User_Id, Last_Activity, Streak_Count) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setDate(2, Date.valueOf(today));
                        insertStmt.setInt(3, 1);
                        insertStmt.executeUpdate();
                        streakCount = 1;
                    }
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return streakCount;
    }
}


