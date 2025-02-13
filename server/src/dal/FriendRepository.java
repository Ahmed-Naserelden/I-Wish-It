package dal;

import connection.FriendPayload;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.Server;

public class FriendRepository {

    public static ArrayList<FriendPayload> getFriends(int memberId) {
        ArrayList<FriendPayload> friends = new ArrayList<>();

        String query = ""
                + "SELECT FRIEND_ID, FRIEND_NAME, FRIEND_DOB, FRIEND_IMAGE "
                + "FROM FRIENDS_VIEW "
                + "WHERE MEMBER_ID = ? "
                + "ORDER BY FRIEND_NAME ASC";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FriendPayload friend = new FriendPayload(
                        rs.getInt("FRIEND_ID"),
                        rs.getString("FRIEND_NAME"),
                        rs.getString("FRIEND_DOB"),
                        rs.getString("FRIEND_IMAGE")
                );
                friends.add(friend);
            }
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }

        return friends;
    }

    public static boolean sendFriendRequest(int memberId, int friendId) {
        String query = "INSERT INTO FRIENDSHIP (MEMBER_ID, FRIEND_ID) VALUES (?, ?)";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, friendId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }

    public static int getMemberIdByEmail(String email) {
        String query = "SELECT ID FROM MEMBER WHERE EMAIL = LOWER(?)";
        try (PreparedStatement stmt = DbManager.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return 0;

    }

    public static ArrayList<FriendPayload> getFriendRequests(int memberId) {
        String query = ""
                + "SELECT MEMBER_ID, MEMBER_NAME, MEMBER_DOB, MEMBER_IMAGE "
                + "FROM FRIEND_REQUESTS_VIEW WHERE FRIEND_ID = ?";

        ArrayList<FriendPayload> friends = new ArrayList<>();

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FriendPayload friend = new FriendPayload(
                        rs.getInt("MEMBER_ID"),
                        rs.getString("MEMBER_NAME"),
                        rs.getString("MEMBER_DOB"),
                        rs.getString("MEMBER_IMAGE")
                );
                friends.add(friend);
            }
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }
        return friends;
    }

    public static boolean acceptFriendRequest(int memberId, int friendId) {
        String query = ""
                + "UPDATE FRIENDSHIP SET STATUS = 'FRIEND' "
                + "WHERE FRIEND_ID = ? AND MEMBER_ID = ?";
        Server.logInfo("MID: " + memberId);
        Server.logInfo("FID: " + friendId);
        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, friendId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }

    public static boolean declineFriendRequest(int memberId, int friendId) {
        String query = ""
                + "DELETE FROM FRIENDSHIP "
                + "WHERE ((MEMBER_ID = ? AND FRIEND_ID = ?) OR "
                + "       (MEMBER_ID = ? AND FRIEND_ID = ?)) AND "
                + "      STATUS = 'PENDING'";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, memberId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }

    public static boolean removeFriend(int memberId, int friendId) {
        String query = ""
                + "DELETE FROM FRIENDSHIP "
                + "WHERE (MEMBER_ID = ? AND FRIEND_ID = ?) OR "
                + "      (MEMBER_ID = ? AND FRIEND_ID = ?)";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, memberId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }
}
