package connection;

import java.io.Serializable;

public class RechargePayload implements Serializable {

    private final int memberId;
    private final int amount;

    public RechargePayload(int userId, int amount) {
        this.memberId = userId;
        this.amount = amount;
    }

    public int getMemberID() {
        return memberId;
    }

    public int getAmount() {
        return amount;
    }
}
