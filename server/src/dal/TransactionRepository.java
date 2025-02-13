package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import server.Server;

public class TransactionRepository {

    public static boolean updateBalance(int memberId, int amount) {
        String query = ""
                + "UPDATE MEMBER SET BALANCE = BALANCE + ? "
                + "WHERE ID = ? AND BALANCE + ? >= 0";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, memberId);
            stmt.setInt(3, amount);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }
        return false;
    }

    public static int getBalance(int memberId) {
        String query = "SELECT BALANCE FROM MEMBER WHERE ID = ?";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("BALANCE");
            }
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }
        return -1;
    }

    public static boolean contribute(int fromMemberId, int toMemberId, int productId, int amount) {
        String query = "INSERT INTO CONTRIBUTION (FROM_MEMBER_ID, TO_MEMBER_ID, PRODUCT_ID, AMOUNT) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, fromMemberId);
            stmt.setInt(2, toMemberId);
            stmt.setInt(3, productId);
            stmt.setInt(4, amount);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }

    public static void refund_contributors(int to_member_id, int productId) {
        String query = ""
                + "UPDATE MEMBER                        "
                + "SET BALANCE = BALANCE + (            "
                + "    SELECT COALESCE(SUM(AMOUNT), 0)  "
                + "    FROM CONTRIBUTION                "
                + "    WHERE FROM_MEMBER_ID = MEMBER.ID "
                + "    AND TO_MEMBER_ID = ?             "
                + "    AND PRODUCT_ID = ?               "
                + ")                                    "
                + "WHERE EXISTS (                       "
                + "    SELECT 1 FROM CONTRIBUTION       "
                + "    WHERE FROM_MEMBER_ID = MEMBER.ID "
                + "    AND TO_MEMBER_ID = ?             "
                + "    AND PRODUCT_ID = ?               "
                + ")                                    ";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, to_member_id);
            stmt.setInt(2, productId);
            stmt.setInt(3, to_member_id);
            stmt.setInt(4, productId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
    }
}
