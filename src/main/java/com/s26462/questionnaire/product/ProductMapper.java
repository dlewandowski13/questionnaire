package com.s26462.questionnaire.product;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * The type Product mapper.
 */
@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    /**
     * Product dto to entity mapper product.
     *
     * @param productDto the product dto
     * @return the product
     */
    public Product productDtoToEntityMapper(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    /**
     * Product to dto mapper product dto.
     *
     * @param product the product
     * @return the product dto
     */
    public ProductDto productToDtoMapper(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

}
