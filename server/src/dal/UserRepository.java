package dal;

import connection.MemberPayload;
import java.sql.*;
import server.Server;

/**
 * UserRepository class to manage user-related database operations.
 * Handles user sign-in and sign-up actions.
 */
public class UserRepository {

    /**
     * Signs in a user with the specified email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A MemberPayload object representing the signed-in user, or null if the sign-in failed.
     */
    public static MemberPayload signIn(String email, String password) {
        // Query to select user details based on email and password
        String query = ""
                + "SELECT ID, EMAIL, USERNAME, PASSWORD, DOB, IMAGE "
                + "FROM MEMBER "
                + "WHERE EMAIL = LOWER(?) AND PASSWORD = ?";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new MemberPayload(
                        rs.getInt("ID"),
                        rs.getString("EMAIL"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("DOB"),
                        rs.getString("IMAGE")
                );
            }
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
            return null;
        }

        return null;
    }

    /**
     * Signs up a new user with the specified email, username, password, and date of birth.
     *
     * @param email The email of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param dob The date of birth of the user.
     * @return True if the sign-up was successful, false otherwise.
     */
    public static boolean signUp(String email, String username, String password, String dob) {
        // Query to insert a new user into the MEMBER table
        String query = ""
                + "INSERT INTO MEMBER (EMAIL, USERNAME, PASSWORD, DOB) "
                + "VALUES (LOWER(?), ?, ?, ?)";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, dob);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
            return false;
        }
    }
}
