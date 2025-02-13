package dal;

import connection.ProductPayload;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.Server;

public class ProductRepository {

    public static ArrayList<ProductPayload> getMarketplace() {
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

    public static int getProductPrice(int productId) {
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
