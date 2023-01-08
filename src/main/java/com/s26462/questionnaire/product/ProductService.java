package com.s26462.questionnaire.product;


import com.s26462.questionnaire.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    public void setProductRepository() {
//    }

    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public void saveProducts(List<Product> products) {
        productRepository.saveAll(products);
    }
    public void insertProducts(List<Product> products) {
        productRepository.saveAll(products);
//        products.stream()
//                .map(product -> productRepository.insert(product));
    }
    public void insertProduct(Product product) {
        productRepository.insert(product);
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
