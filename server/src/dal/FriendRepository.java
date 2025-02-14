package dal;

import connection.FriendPayload;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.Server;

/**
 * FriendRepository class to manage friend-related database operations.
 * Handles retrieving friends, sending friend requests, and managing friend requests.
 */
public class FriendRepository {

    /**
     * Retrieves the list of friends for a given member.
     *
     * @param memberId The ID of the member.
     * @return An ArrayList of FriendPayload objects representing the friends.
     */
    public static ArrayList<FriendPayload> getFriends(int memberId) {
        // Query to select friends of a member
        String query = ""
                + "SELECT FRIEND_ID, FRIEND_NAME, FRIEND_DOB, FRIEND_IMAGE "
                + "FROM FRIENDS_VIEW "
                + "WHERE MEMBER_ID = ? "
                + "ORDER BY FRIEND_NAME ASC";

        ArrayList<FriendPayload> friends = new ArrayList<>();

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

    /**
     * Sends a friend request from one member to another.
     *
     * @param memberId The ID of the member sending the request.
     * @param friendId The ID of the member receiving the request.
     * @return True if the request was sent successfully, false otherwise.
     */
    public static boolean sendFriendRequest(int memberId, int friendId) {
        // Query to insert a new friend request
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

    /**
     * Retrieves the member ID associated with a given email.
     *
     * @param email The email of the member.
     * @return The ID of the member, or 0 if not found.
     */
    public static int getMemberIdByEmail(String email) {
        // Query to select member ID by email
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

    /**
     * Retrieves the list of friend requests for a given member.
     *
     * @param memberId The ID of the member.
     * @return An ArrayList of FriendPayload objects representing the friend requests.
     */
    public static ArrayList<FriendPayload> getFriendRequests(int memberId) {
        // Query to select friend requests for a member
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

    /**
     * Accepts a friend request between two members.
     *
     * @param memberId The ID of the member accepting the request.
     * @param friendId The ID of the member whose request is being accepted.
     * @return True if the request was accepted successfully, false otherwise.
     */
    public static boolean acceptFriendRequest(int memberId, int friendId) {
        // Query to update the status of a friend request to 'FRIEND'
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

    /**
     * Declines a friend request between two members.
     *
     * @param memberId The ID of the member declining the request.
     * @param friendId The ID of the member whose request is being declined.
     * @return True if the request was declined successfully, false otherwise.
     */
    public static boolean declineFriendRequest(int memberId, int friendId) {
        // Query to delete a pending friend request
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

    /**
     * Removes a friend relationship between two members.
     *
     * @param memberId The ID of the member removing the friend.
     * @param friendId The ID of the friend being removed.
     * @return True if the friend was removed successfully, false otherwise.
     */
    public static boolean removeFriend(int memberId, int friendId) {
        // Query to delete a friend relationship
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
