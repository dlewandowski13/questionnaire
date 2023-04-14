package com.s26462.questionnaire.product;

import com.s26462.questionnaire.QuestionnaireApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Kontroler obsługujący produkty /products
 *
 * @author Dawid Lewandowski
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(QuestionnaireApplication.class);

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * Metoda obsługująca GET:/products
     *
     * @return lista wszystkich produktów
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<Product> products = productService.getProducts();
        List<ProductDto> productsDto = products.stream()
                .map(productMapper::productToDtoMapper)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsDto);
    }

    /**
     * Metoda obsługująca GET:/products/{productSymbol}
     *
     * @param productSymbol - unikalny symbol produktu
     * @return w przypadku znalezienia produktu o podanym symbolu zwraca produkt, w przeciwnym wypadku zwraca notFound
     */
    @GetMapping("/{productSymbol}")
    public ResponseEntity<ProductDto> getProductBySymbol(@PathVariable(value = "productSymbol") String productSymbol) {
        return productService.getProductBySymbol(productSymbol)
                .map(productMapper::productToDtoMapper)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Metoda obsługująca POST:/products
     *
     * @param productsDto - lista obiektów produktów w formacie json
     * @return ok
     */

    @PostMapping
    public ResponseEntity<Void> postProducts(@RequestBody List<ProductDto> productsDto) {
        List<Product> products = productsDto.stream()
                .map(productMapper::productDtoToEntityMapper)
                .collect(Collectors.toList());
        productService.insertProducts(products);
        return ResponseEntity.ok().build();
    }

    /**
     * Metoda obsługująca POST:/products/product
     *
     * @param productDto - obiekt produktu w formacie json
     * @return ok
     */
    @PostMapping("/product")
    public ResponseEntity<Void> postProducts(@RequestBody ProductDto productDto) {
        Product product = productMapper.productDtoToEntityMapper(productDto);
        productService.insertProduct(product);
        return ResponseEntity.ok().build();
    }

    /**
     * Metoda obsługująca PUT:/products/productSymbol
     *
     * @param productSymbol - unikalny symbol produktu
     * @param productDto    - obiekt produktu, który ma zostać zaktualizowany
     * @return ok lub notFound
     */

    @PutMapping("/{productSymbol}")
    public ResponseEntity<Void> putProducts(@PathVariable String productSymbol, @RequestBody ProductDto productDto) {
        Optional<Product> currentProduct = productService.getProductById(productSymbol);

        if (currentProduct.isPresent()) {
            Product product = productMapper.productDtoToEntityMapper(productDto);
            productService.updateProductById(productSymbol, product);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}

