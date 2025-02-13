package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import connection.NotificationPayload;
import server.Server;

public class NotificationRepository {

    public static ArrayList<NotificationPayload> get(int memberId) {
        String query = ""
                + "SELECT ID, INFO, TIMESTAMP FROM NOTIFICATION "
                + "WHERE MEMBER_ID = ?";
        ArrayList<NotificationPayload> notifications = new ArrayList<>();

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NotificationPayload notificationn = new NotificationPayload(
                        rs.getInt("ID"),
                        rs.getString("INFO"),
                        rs.getTimestamp("TIMESTAMP")
                );
                notifications.add(notificationn);
            }

        } catch (SQLException e) {
            Server.logFail(e.toString());
        }

        return notifications;
    }

    public static boolean delete(int id) {
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
