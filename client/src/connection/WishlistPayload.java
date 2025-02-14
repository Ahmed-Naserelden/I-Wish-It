package connection;

import java.io.Serializable;

/**
 * WishlistPayload class to represent the data for a wishlist action.
 * Implements Serializable for object serialization.
 */
public class WishlistPayload implements Serializable {

    private final int memberId; // ID of the member
    private final int productId; // ID of the product

    /**
     * Constructs a new WishlistPayload with the specified member ID and product ID.
     *
     * @param memberId ID of the member.
     * @param productId ID of the product.
     */
    public WishlistPayload(int memberId, int productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    /**
     * Gets the ID of the member.
     *
     * @return The ID of the member.
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Gets the ID of the product.
     *
     * @return The ID of the product.
     */
    public int getProductId() {
        return productId;
    }
}
