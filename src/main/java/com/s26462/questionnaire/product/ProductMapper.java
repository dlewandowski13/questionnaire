package com.s26462.questionnaire.product;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public Product productDtoToEntityMapper(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    public ProductDto productToDtoMapper(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

}
