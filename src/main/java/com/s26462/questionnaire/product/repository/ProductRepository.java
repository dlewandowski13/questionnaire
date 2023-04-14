package com.s26462.questionnaire.product.repository;

import com.s26462.questionnaire.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ symbol: ?0 }")
    Optional<Product> findBySymbol(String symbol);
}
