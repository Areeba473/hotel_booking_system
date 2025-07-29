package Hotel.background;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Hotel.DatabaseConnection; // ✅ Corrected import

public class ActivityLogger {

    public static void log(String username, String action) {
        String sql = "INSERT INTO UserActivityLog (username, action) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, action);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
