package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import server.Server;

/**
 * TransactionRepository class to manage transaction-related database operations.
 * Handles updating balances, retrieving balances, contributing to products, and refunding contributors.
 */
public class TransactionRepository {

    /**
     * Updates the balance of a member by a specified amount.
     *
     * @param memberId The ID of the member.
     * @param amount The amount to update the balance by.
     * @return True if the balance was updated successfully, false otherwise.
     */
    public static boolean updateBalance(int memberId, int amount) {
        // Query to update the balance of a member
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

    /**
     * Retrieves the balance of a member.
     *
     * @param memberId The ID of the member.
     * @return The balance of the member, or -1 if an error occurred.
     */
    public static int getBalance(int memberId) {
        // Query to select the balance of a member
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

    /**
     * Contributes an amount from one member to another member's product.
     *
     * @param fromMemberId The ID of the member making the contribution.
     * @param toMemberId The ID of the member receiving the contribution.
     * @param productId The ID of the product being contributed to.
     * @param amount The amount of the contribution.
     * @return True if the contribution was made successfully, false otherwise.
     */
    public static boolean contribute(int fromMemberId, int toMemberId, int productId, int amount) {
        // Query to insert a new contribution
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

    /**
     * Refunds contributors for a specific product.
     *
     * @param toMemberId The ID of the member receiving the refund.
     * @param productId The ID of the product being refunded.
     */
    public static void refund_contributors(int toMemberId, int productId) {
        // Query to refund contributors for a specific product
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
            stmt.setInt(1, toMemberId);
            stmt.setInt(2, productId);
            stmt.setInt(3, toMemberId);
            stmt.setInt(4, productId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
    }
}
