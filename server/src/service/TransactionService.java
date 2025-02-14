package service;

import connection.ContributionPayload;
import connection.RechargePayload;
import connection.Response;
import dal.TransactionRepository;
import dal.WishlistRepository;

/**
 * TransactionService class to handle transaction-related operations.
 * Provides methods for managing member balances and contributions.
 */
public class TransactionService {

    /**
     * Retrieves the balance of a member.
     *
     * @param memberId The ID of the member.
     * @return A Response object containing the balance of the member.
     */
    public static Response getBalance(int memberId) {
        int balance = TransactionRepository.getBalance(memberId);
        return new Response(balance != -1, balance);
    }

    /**
     * Charges the balance of a member by a specified amount.
     *
     * @param payload The RechargePayload object containing the member ID and charge amount.
     * @return A Response object containing the status of the balance charge.
     */
    public static Response chargeBalance(RechargePayload payload) {
        int memberId = payload.getMemberID();
        int chargeAmount = payload.getAmount();

        boolean status = TransactionRepository.updateBalance(memberId, chargeAmount);

        if (status) {
            return new Response(true);
        } else {
            return new Response(false);
        }
    }

    /**
     * Contributes an amount from one member to another member's product.
     * Validates the balance and remaining amount needed for the product.
     *
     * @param payload The ContributionPayload object containing the contribution details.
     * @return A Response object containing the status of the contribution and the contributed amount.
     */
    public static Response contribute(ContributionPayload payload) {
        int balance = TransactionRepository.getBalance(payload.getFromMemberId());
        if (balance < payload.getAmount()) {
            return new Response(false, "Insufficient balance!");
        }
        int remaining = WishlistRepository.getRemaining(
                payload.getToMemberId(),
                payload.getProductId()
        );
        int amount = payload.getAmount() > remaining ? remaining : payload.getAmount();

        if (TransactionRepository.contribute(
                payload.getFromMemberId(),
                payload.getToMemberId(),
                payload.getProductId(),
                amount
        )) {
            TransactionRepository.updateBalance(payload.getFromMemberId(), -amount);
            return new Response(true, amount);
        } else {
            return new Response(false, "Cannot add contribution!");
        }
    }
}
