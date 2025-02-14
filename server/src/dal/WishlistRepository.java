package dal;

import connection.ProductPayload;
import java.sql.*;
import java.util.ArrayList;
import server.Server;

/**
 * WishlistRepository class to manage wishlist-related database operations.
 * Handles retrieving, adding, and deleting wishlist items.
 */
public class WishlistRepository {

    /**
     * Retrieves the list of products in the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @return An ArrayList of ProductPayload objects representing the products in the wishlist.
     */
    public static ArrayList<ProductPayload> get(int memberId) {
        // Query to select products in the wishlist for a member
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

    /**
     * Adds a product to the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @param productId The ID of the product to add.
     * @return True if the product was added successfully, false otherwise.
     */
    public static boolean add(int memberId, int productId) {
        // Query to insert a new product into the wishlist
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

    /**
     * Deletes a product from the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @param productId The ID of the product to delete.
     * @return True if the product was deleted successfully, false otherwise.
     */
    public static boolean delete(int memberId, int productId) {
        // Query to delete a product from the wishlist
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

    /**
     * Retrieves the remaining amount needed for a product in the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @param productId The ID of the product.
     * @return The remaining amount needed for the product, or -1 if an error occurred.
     */
    public static int getRemaining(int memberId, int productId) {
        // Query to select the remaining amount needed for a product in the wishlist
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
