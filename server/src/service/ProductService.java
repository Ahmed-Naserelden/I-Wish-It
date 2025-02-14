package service;

import connection.ProductPayload;
import connection.Response;
import dal.ProductRepository;
import java.util.ArrayList;

/**
 * ProductService class to handle product-related operations.
 * Provides methods for managing products in the marketplace.
 */
public class ProductService {

    /**
     * Retrieves the list of products available in the marketplace.
     *
     * @return A Response object containing the list of products.
     */
    public static Response getMarketplace() {
        ArrayList<ProductPayload> products = ProductRepository.getMarketplace();
        return new Response(true, products);
    }

    /**
     * Retrieves the price of a product by its ID.
     *
     * @param productId The ID of the product.
     * @return A Response object containing the price of the product.
     */
    public static Response getProductPrice(int productId) {
        int price = ProductRepository.getProductPrice(productId);
        return new Response(price != -1, price);
    }
}
