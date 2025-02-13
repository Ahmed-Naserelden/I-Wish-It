package service;

import connection.RechargePayload;
import connection.Response;
import dal.TransactionRepository;
import connection.ContributionPayload;
import dal.WishlistRepository;

public class TransactionService {

    public static Response getBalance(int memberId) {
        int balance = TransactionRepository.getBalance(memberId);
        return new Response(balance != -1, balance);
    }

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
