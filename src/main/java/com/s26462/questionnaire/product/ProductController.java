package com.s26462.questionnaire.product;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Kontroler obsługujący produkty /products
 *
 * @author Dawid Lewandowski
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Metoda obsługująca GET:/products
     *
     * @return lista wszystkich produktów
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    /**
     * Metoda obsługująca GET:/products/{productSymbol}
     *
     * @param productSymbol - unikalny symbol produktu
     * @return w przypadku znalezienia produktu o podanym symbolu zwraca produkt, w przeciwnym wypadku zwraca notFound
     */
    @GetMapping("/product/{productSymbol}")
    public ResponseEntity<ProductDto> getProductBySymbol(@PathVariable(value = "productSymbol") String productSymbol) {
        return productService.getProductBySymbol(productSymbol)
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> postProducts(@RequestBody List<ProductDto> productsDto) {
        productService.insertProducts(productsDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Metoda obsługująca POST:/products/product
     *
     * @param productDto - obiekt produktu w formacie json
     * @return ok
     */
    @PostMapping("/product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> postProduct(@RequestBody ProductDto productDto) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{productSymbol}")
                .buildAndExpand(productService.insertProduct(productDto).getSymbol())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Metoda obsługująca PUT:/products/productSymbol
     *
     * @param productSymbol - unikalny symbol produktu
     * @param productDto    - obiekt produktu, który ma zostać zaktualizowany
     * @return ok lub notFound
     */

    @PutMapping("/product/{productSymbol}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> putProducts(@PathVariable String productSymbol, @RequestBody ProductDto productDto) {
        return productService.updateProductBySymbol(productSymbol, productDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/product/{productSymbol}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productSymbol) {
        productService.deleteProductBySymbol(productSymbol);
        return ResponseEntity.noContent().build();
    }
}

