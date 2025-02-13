package dal;

import connection.MemberPayload;
import java.sql.*;
import server.Server;

public class UserRepository {

    public static MemberPayload signIn(String email, String password) {
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

    public static boolean signUp(String email, String username, String password, String dob) {
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
