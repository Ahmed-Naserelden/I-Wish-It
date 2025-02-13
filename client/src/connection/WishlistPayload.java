package connection;

import java.io.Serializable;

public class WishlistPayload implements Serializable {

    private final int memberId;
    private final int productId;

    public WishlistPayload(int memberId, int productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getProductId() {
        return productId;
    }
}
