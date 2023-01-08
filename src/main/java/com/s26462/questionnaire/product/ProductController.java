package com.s26462.questionnaire.product;

import com.s26462.questionnaire.QuestionnaireApplication;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static Logger logger = LogManager.getLogger(QuestionnaireApplication.class);
    @Autowired
    private ProductService productService;
    private final ProductMapper productMapper;

//    @Autowired
//    public void setProductService() {
//        this.productService = productService;
//    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<Product> products = productService.getProducts();
        List<ProductDto> productsDto = products.stream()
                .map(productMapper::productToDtoMapper)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsDto);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductsBySymbol(@PathVariable(value = "productId") String productId) {
        return productService.getProductById(productId)
                .map(productMapper::productToDtoMapper)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> postProducts(@RequestBody List<ProductDto> productsDto) {
        logger.info("productsDto");
        logger.info(productsDto);
        System.out.println("productsDto");
        System.out.println(productsDto);
        List<Product> products = productsDto.stream()
                        .map(productMapper::productDtoToEntityMapper)
                        .collect(Collectors.toList());
        System.out.println("products");
        System.out.println(products);
        productService.insertProducts(products);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/product")
    public ResponseEntity<Void> postProducts(@RequestBody ProductDto productDto) {
        logger.info("productDto");
        logger.info(productDto);
        System.out.println("productDto");
        System.out.println(productDto);
        Product product  = productMapper.productDtoToEntityMapper(productDto);
        System.out.println("products");
        System.out.println(product.toString());
        productService.insertProduct(product);
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

