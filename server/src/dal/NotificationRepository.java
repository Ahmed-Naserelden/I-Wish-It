package dal;

import connection.NotificationPayload;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.Server;

/**
 * NotificationRepository class to manage notification-related database operations.
 * Handles retrieving and deleting notifications.
 */
public class NotificationRepository {

    /**
     * Retrieves the list of notifications for a given member.
     *
     * @param memberId The ID of the member.
     * @return An ArrayList of NotificationPayload objects representing the notifications.
     */
    public static ArrayList<NotificationPayload> get(int memberId) {
        // Query to select notifications for a member
        String query = ""
                + "SELECT ID, INFO, TIMESTAMP FROM NOTIFICATION "
                + "WHERE MEMBER_ID = ?";
        ArrayList<NotificationPayload> notifications = new ArrayList<>();

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NotificationPayload notification = new NotificationPayload(
                        rs.getInt("ID"),
                        rs.getString("INFO"),
                        rs.getTimestamp("TIMESTAMP")
                );
                notifications.add(notification);
            }

        } catch (SQLException e) {
            Server.logFail(e.toString());
        }

        return notifications;
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param id The ID of the notification to delete.
     * @return True if the notification was deleted successfully, false otherwise.
     */
    public static boolean delete(int id) {
        // Query to delete a notification by ID
        String query = "DELETE FROM NOTIFICATION WHERE ID = ?";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, id);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }
}
