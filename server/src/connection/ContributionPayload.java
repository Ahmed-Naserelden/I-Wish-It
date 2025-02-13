package connection;

import java.io.Serializable;

public class ContributionPayload implements Serializable {

    private final int fromMemberId;
    private final int toMemberId;
    private final int productId;
    private final int amount;

    public ContributionPayload(int fromMemberId, int toMemberId, int productId, int amount) {
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.productId = productId;
        this.amount = amount;
    }

    public int getFromMemberId() {
        return fromMemberId;
    }

    public int getToMemberId() {
        return toMemberId;
    }

    public int getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }
}
