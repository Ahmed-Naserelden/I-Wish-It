package dal;

import connection.ProductPayload;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.Server;

/**
 * ProductRepository class to manage product-related database operations.
 * Handles retrieving products from the marketplace and getting product prices.
 */
public class ProductRepository {

    /**
     * Retrieves the list of products available in the marketplace.
     *
     * @return An ArrayList of ProductPayload objects representing the products.
     */
    public static ArrayList<ProductPayload> getMarketplace() {
        // Query to select all products from the marketplace
        String query = "SELECT ID, NAME, BRAND, INFO, PRICE, IMAGE FROM PRODUCT";
        ArrayList<ProductPayload> products = new ArrayList<>();

        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductPayload product = new ProductPayload(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getString("BRAND"),
                        rs.getString("INFO"),
                        rs.getInt("PRICE"),
                        rs.getString("IMAGE")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            Server.logFail(e.toString());
        }

        return products;
    }

    /**
     * Retrieves the price of a product by its ID.
     *
     * @param productId The ID of the product.
     * @return The price of the product, or -1 if the product is not found.
     */
    public static int getProductPrice(int productId) {
        // Query to select the price of a product by ID
        String query = "SELECT PRICE FROM PRODUCT WHERE ID = ?";
        try {
            PreparedStatement stmt = DbManager.prepareStatement(query);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("PRICE");
            }
        } catch (SQLException e) {
            Server.logFail(e.toString());
        }
        return -1;
    }
}
