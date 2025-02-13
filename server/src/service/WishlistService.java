package service;

import connection.Response;
import connection.WishlistPayload;
import dal.WishlistRepository;
import java.util.ArrayList;
import connection.ProductPayload;
import dal.TransactionRepository;

public class WishlistService {

    public static Response get(int memberId) {
        ArrayList<ProductPayload> products = WishlistRepository.get(memberId);
        return new Response(true, products);
    }

    public static Response add(WishlistPayload payload) {
        boolean status = WishlistRepository.add(
                payload.getMemberId(),
                payload.getProductId()
        );
        return new Response(status);
    }

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
