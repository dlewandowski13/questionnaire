package com.s26462.questionnaire.product;


import com.s26462.questionnaire.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Product service.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository the product repository
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Gets product by id.
     *
     * @param productId the product id
     * @return the product by id
     */
    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    /**
     * Gets product by symbol.
     *
     * @param productSymbol the product symbol
     * @return the product by symbol
     */
    public Optional<Product> getProductBySymbol(String productSymbol) {
        return productRepository.findBySymbol(productSymbol);
    }

    /**
     * Gets products.
     *
     * @return the products
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Insert products.
     *
     * @param products the products
     */
    public void insertProducts(List<Product> products) {
        productRepository.saveAll(products);
    }

    /**
     * Insert product.
     *
     * @param product the product
     */
    public void insertProduct(Product product) {
        productRepository.insert(product);
    }

    /**
     * Update product by id.
     *
     * @param productId the product id
     * @param product   the product
     */
    public void updateProductById(String productId, Product product) {
        productRepository.findById(productId).ifPresent(foundProduct -> {
            foundProduct.setSymbol(product.getSymbol());
            foundProduct.setName(product.getName());
            foundProduct.setActive(product.isActive());
            productRepository.save(foundProduct);
        });
    }
}
