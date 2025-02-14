package service;

import connection.ProductPayload;
import connection.Response;
import dal.WishlistRepository;
import java.util.ArrayList;

/**
 * WishlistService class to handle wishlist-related operations.
 * Provides methods for managing wishlist items.
 */
public class WishlistService {

    /**
     * Retrieves the list of products in the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @return A Response object containing the list of products in the wishlist.
     */
    public static Response getWishlist(int memberId) {
        ArrayList<ProductPayload> products = WishlistRepository.get(memberId);
        return new Response(true, products);
    }

    /**
     * Adds a product to the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @param productId The ID of the product to add.
     * @return A Response object containing the status of the addition.
     */
    public static Response addToWishlist(int memberId, int productId) {
        boolean status = WishlistRepository.add(memberId, productId);
        return new Response(status);
    }

    /**
     * Deletes a product from the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @param productId The ID of the product to delete.
     * @return A Response object containing the status of the deletion.
     */
    public static Response deleteFromWishlist(int memberId, int productId) {
        boolean status = WishlistRepository.delete(memberId, productId);
        return new Response(status);
    }

    /**
     * Retrieves the remaining amount needed for a product in the wishlist for a given member.
     *
     * @param memberId The ID of the member.
     * @param productId The ID of the product.
     * @return A Response object containing the remaining amount needed for the product.
     */
    public static Response getRemainingAmount(int memberId, int productId) {
        int remaining = WishlistRepository.getRemaining(memberId, productId);
        return new Response(remaining != -1, remaining);
    }
}
