package dal;

import connection.ProductPayload;
import java.sql.*;
import java.util.ArrayList;
import server.Server;

public class WishlistRepository {

    public static ArrayList<ProductPayload> get(int memberId) {
        String query = ""
                + "SELECT PRODUCT_ID, NAME, BRAND, INFO, PRICE, IMAGE, TOTAL_CONTRIBUTION "
                + "FROM WISHLIST_VIEW "
                + "WHERE MEMBER_ID = ?";

        ArrayList<ProductPayload> products = new ArrayList<>();

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductPayload product = new ProductPayload(
                        rs.getInt("PRODUCT_ID"),
                        rs.getString("NAME"),
                        rs.getString("BRAND"),
                        rs.getString("INFO"),
                        rs.getInt("PRICE"),
                        rs.getString("IMAGE"),
                        rs.getInt("TOTAL_CONTRIBUTION")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }
        return products;
    }

    public static boolean add(int memberId, int productId) {
        String query = ""
                + "INSERT INTO WISHLIST "
                + "(MEMBER_ID, PRODUCT_ID) VALUES (?, ?)";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, productId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
            return false;
        }
    }

    public static boolean delete(int memberId, int productId) {
        String query = ""
                + "DELETE FROM WISHLIST "
                + "WHERE MEMBER_ID = ? AND PRODUCT_ID = ?";

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, productId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
            return false;
        }
    }

    public static int getRemaining(int memberId, int productId) {
        String query = ""
                + "SELECT REMAINING FROM WISHLIST_VIEW "
                + "WHERE MEMBER_ID = ? AND PRODUCT_ID = ?";
        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("REMAINING");
            }
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }
        return -1;
    }
}
