package service;

import connection.ProductPayload;
import connection.Response;
import connection.WishlistPayload;
import dal.TransactionRepository;
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
    public static Response get(int memberId) {
        ArrayList<ProductPayload> products = WishlistRepository.get(memberId);
        return new Response(true, products);
    }

    /**
     * Adds a product to the wishlist for a given member.
     *
     * @param payload The WishlistPayload object containing the member ID and product ID.
     * @return A Response object containing the status of the addition.
     */
    public static Response add(WishlistPayload payload) {
        boolean status = WishlistRepository.add(
                payload.getMemberId(),
                payload.getProductId()
        );
        return new Response(status);
    }

    /**
     * Deletes a product from the wishlist for a given member.
     * Refunds contributors if the product is not fully funded.
     *
     * @param payload The WishlistPayload object containing the member ID and product ID.
     * @return A Response object containing the status of the deletion.
     */
    public static Response delete(WishlistPayload payload) {
        int memberId = payload.getMemberId();
        int productId = payload.getProductId();
        int remaining = WishlistRepository.getRemaining(memberId, productId);

        if (remaining > 0) {
            TransactionRepository.refund_contributors(memberId, productId);
        } else {
            // full-funded; do nothing!
        }

        return new Response(WishlistRepository.delete(memberId, productId));
    }
}
