package service;

import dal.ProductRepository;
import connection.Response;

public class ProductService {

    public static Response getMarketplace() {
        return new Response(true, ProductRepository.getMarketplace());
    }
}
