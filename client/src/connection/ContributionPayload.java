package connection;

import java.io.Serializable;

/**
 * ContributionPayload class to represent the data for a contribution.
 * Implements Serializable for object serialization.
 */
public class ContributionPayload implements Serializable {

    private final int fromMemberId; // ID of the member making the contribution
    private final int toMemberId; // ID of the member receiving the contribution
    private final int productId; // ID of the product being contributed to
    private final int amount; // Amount of the contribution

    /**
     * Constructs a new ContributionPayload with the specified details.
     *
     * @param fromMemberId ID of the member making the contribution.
     * @param toMemberId ID of the member receiving the contribution.
     * @param productId ID of the product being contributed to.
     * @param amount Amount of the contribution.
     */
    public ContributionPayload(int fromMemberId, int toMemberId, int productId, int amount) {
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.productId = productId;
        this.amount = amount;
    }

    /**
     * Gets the ID of the member making the contribution.
     *
     * @return The ID of the member making the contribution.
     */
    public int getFromMemberId() {
        return fromMemberId;
    }

    /**
     * Gets the ID of the member receiving the contribution.
     *
     * @return The ID of the member receiving the contribution.
     */
    public int getToMemberId() {
        return toMemberId;
    }

    /**
     * Gets the ID of the product being contributed to.
     *
     * @return The ID of the product being contributed to.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Gets the amount of the contribution.
     *
     * @return The amount of the contribution.
     */
    public int getAmount() {
        return amount;
    }
}
