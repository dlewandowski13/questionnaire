package com.s26462.questionnaire.product;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Product service.
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository the product repository
     */
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Gets product by id.
     *
     * @param productId the product id
     * @return the product by id
     */
    public Optional<ProductDto> getProductById(String productId) {
        return Optional.ofNullable(productId)
                        .flatMap(productRepository::findBySymbol)
                        .map(productMapper::productToDtoMapper);
    }

    /**
     * Gets product by symbol.
     *
     * @param productSymbol the product symbol
     * @return the product by symbol
     */
    public Optional<ProductDto> getProductBySymbol(String productSymbol) {
        return Optional.ofNullable(productSymbol)
                .flatMap(productRepository::findBySymbol)
                .map(productMapper::productToDtoMapper);
    }


    /**
     * Gets products.
     *
     * @return the products
     */
    public Optional<List<ProductDto>> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productsDto = products.stream()
                .map(productMapper::productToDtoMapper)
                .collect(Collectors.toList());
        return Optional.of(productsDto);
    }

    /**
     * Insert products.
     *
     * @param products the products
     */
    public void insertProducts(List<ProductDto> products) {
        productRepository.saveAll(products.stream()
                .map(productMapper::productDtoToEntityMapper)
                .collect(Collectors.toList()));
    }


    /**
     * Insert product.
     *
     * @param productDto the product
     */
    public void insertProduct(ProductDto productDto) {
        productRepository.insert(productMapper.productDtoToEntityMapper(productDto));
    }

    /**
     * Update product by id.
     *
     * @param productSymbol the product id
     * @param productDto   the product
     */
    public Optional<ProductDto> updateProductBySymbol(String productSymbol, ProductDto productDto) {
        return productRepository.findBySymbol(productSymbol)
                .map(existingProduct -> {
                    Product updatedProduct = productMapper.productDtoToEntityMapper(productDto);
                    updatedProduct.setSymbol(productSymbol);
                    productRepository.save(updatedProduct);
                    return productMapper.productToDtoMapper(updatedProduct);
                });
    }
}
