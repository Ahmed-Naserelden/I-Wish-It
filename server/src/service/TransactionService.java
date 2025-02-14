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
     * Updates the balance of a member by a specified amount.
     *
     * @param memberId The ID of the member.
     * @param amount The amount to update the balance by.
     * @return A Response object containing the status of the balance update.
     */
    public static Response updateBalance(int memberId, int amount) {
        boolean status = TransactionRepository.updateBalance(memberId, amount);
        return new Response(status);
    }

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
     * Contributes an amount from one member to another member's product.
     *
     * @param fromMemberId The ID of the member making the contribution.
     * @param toMemberId The ID of the member receiving the contribution.
     * @param productId The ID of the product being contributed to.
     * @param amount The amount of the contribution.
     * @return A Response object containing the status of the contribution.
     */
    public static Response contribute(int fromMemberId, int toMemberId, int productId, int amount) {
        boolean status = TransactionRepository.contribute(fromMemberId, toMemberId, productId, amount);
        return new Response(status);
    }

    /**
     * Refunds contributors for a specific product.
     *
     * @param toMemberId The ID of the member receiving the refund.
     * @param productId The ID of the product being refunded.
     * @return A Response object containing the status of the refund.
     */
    public static Response refundContributors(int toMemberId, int productId) {
        TransactionRepository.refund_contributors(toMemberId, productId);
        return new Response(true);
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
