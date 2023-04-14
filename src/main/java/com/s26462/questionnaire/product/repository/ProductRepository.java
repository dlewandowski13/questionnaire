package com.s26462.questionnaire.product.repository;

import com.s26462.questionnaire.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Product repository.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Find by symbol optional.
     *
     * @param productSymbol the product symbol
     * @return the optional
     */
    @Query("{ symbol: ?0 }")
    Optional<Product> findBySymbol(String productSymbol);

    /**
     * Delete product by symbol optional.
     *
     * @param productSymbol the product symbol
     */
    @Query("{ symbol: ?0 }")
    void deleteProductBySymbol(String productSymbol);

}
