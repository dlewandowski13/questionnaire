package com.s26462.questionnaire.product;


import com.s26462.questionnaire.exception.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Product service.
 */
@Service
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository the product repository
     * @param productMapper     the product mapper
     */
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
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
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::productToDtoMapper)
                .collect(Collectors.toList());
    }

    /**
     * Insert products.
     *
     * @param products the products
     */
    public void insertProducts(List<ProductDto> products) {
        checkForDuplicates(products);
        products.stream()
                .map(productMapper::productDtoToEntityMapper)
                .filter(product -> productRepository.findBySymbol(product.getSymbol()).isPresent())
                .findFirst()
                .ifPresent(product -> {
                    throw new DuplicateKeyException("Istnieje już produkt o symbolu: " + product.getSymbol());
                });

        productRepository.saveAll(products.stream()
                .map(productMapper::productDtoToEntityMapper)
                .collect(Collectors.toList()));
    }


    /**
     * Insert product product dto.
     *
     * @param productDto the product dto
     * @return the product dto
     */
    public ProductDto insertProduct(ProductDto productDto) {
        return productMapper.productToDtoMapper(
                productRepository.insert(productMapper.productDtoToEntityMapper(productDto))
        );
    }

    /**
     * Update product by symbol optional.
     *
     * @param productSymbol the product symbol
     * @param productDto    the product dto
     * @return the optional
     */
    public Optional<ProductDto> updateProductBySymbol(String productSymbol, ProductDto productDto) {
        return productRepository.findBySymbol(productSymbol)
                .map(existingProduct -> {
                    Product updatedProduct = productMapper.productDtoToEntityMapper(productDto);
                    updatedProduct.setId(existingProduct.getId());
                    productRepository.save(updatedProduct);
                    return productMapper.productToDtoMapper(updatedProduct);
                });
    }

    /**
     * Delete product by symbol.
     *
     * @param productSymbol the product symbol
     */
    public void deleteProductBySymbol(String productSymbol) {
        productRepository.findBySymbol(productSymbol)
                .ifPresentOrElse(existingProduct -> productRepository.deleteById(existingProduct.getId()), () -> {
                    throw new EntityNotFoundException(String.format("Brak produktu o symbolu %s", productSymbol));
                });
    }

    /**
     * Check for duplicates.
     *
     * @param products the products
     */
    public void checkForDuplicates(List<ProductDto> products) {
        Map<String, Long> duplicates = products.stream()
                .collect(Collectors.groupingBy(ProductDto::getSymbol, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!duplicates.isEmpty()) {
            throw new DuplicateKeyException(
                    String.format("Na podanej liście występuje zduplikowany symbol produktu: %s", duplicates.keySet()));
        }
    }
}
