package com.s26462.questionnaire.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public void setProductService() {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<Product> products = productService.getProducts();
        List<ProductDto> productsDto = products.stream()
                .map(product -> productMapper.productToDtoMapper(product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsDto);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductsBySymbol(@PathVariable(value = "productId") String productId) {
        return productService.getProductById(productId)
                .map(product -> productMapper.productToDtoMapper(product))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> postProducts(@RequestBody List<ProductDto> productsDto) {
        List<Product> products = productsDto.stream()
                        .map(productDto -> productMapper.productDtoToEntityMapper(productDto))
                        .collect(Collectors.toList());
        productService.saveProducts(products);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> putProducts(@PathVariable String productId, @RequestBody ProductDto productDto) {
        Optional<Product> currentProduct = productService.getProductById(productId);

        if(currentProduct.isPresent()) {
            Product product = productMapper.productDtoToEntityMapper(productDto);
            productService.updateProductById(productId, product);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}

