package connection;

import java.io.Serializable;

/**
 * RechargePayload class to represent the data for a recharge action.
 * Implements Serializable for object serialization.
 */
public class RechargePayload implements Serializable {

    private final int memberId; // ID of the member performing the recharge
    private final int amount; // Amount of the recharge

    /**
     * Constructs a new RechargePayload with the specified member ID and amount.
     *
     * @param memberId ID of the member performing the recharge.
     * @param amount Amount of the recharge.
     */
    public RechargePayload(int memberId, int amount) {
        this.memberId = memberId;
        this.amount = amount;
    }

    /**
     * Gets the ID of the member performing the recharge.
     *
     * @return The ID of the member performing the recharge.
     */
    public int getMemberID() {
        return memberId;
    }

    /**
     * Gets the amount of the recharge.
     *
     * @return The amount of the recharge.
     */
    public int getAmount() {
        return amount;
    }
}
