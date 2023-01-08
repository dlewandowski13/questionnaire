package com.s26462.questionnaire.product;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository() {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public void saveProducts(List<Product> products) {
        productRepository.saveAll(products);
    }

    public void updateProductById(String productId, Product product) {
        productRepository.findById(productId).ifPresent(foundProduct -> {
            foundProduct.setSymbol(product.getSymbol());
            foundProduct.setName(product.getName());
            foundProduct.setActive(product.isActive());
            productRepository.save(foundProduct);
        });
    }
}
