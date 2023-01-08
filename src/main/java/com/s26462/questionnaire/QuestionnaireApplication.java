package com.s26462.questionnaire;

import com.s26462.questionnaire.product.Product;
import com.s26462.questionnaire.product.ProductDto;
import com.s26462.questionnaire.product.ProductMapper;
import com.s26462.questionnaire.product.repository.ProductRepository;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootConfiguration
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({ MongoAutoConfiguration.class})
@NoArgsConstructor
public class QuestionnaireApplication {

    ProductMapper productMapper;
    public static void main(String[] args) {
        SpringApplication.run(QuestionnaireApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() { return new ModelMapper(); }

//    @Bean
//    CommandLineRunner commandLineRunner (ProductRepositoryImpl productRepository, MongoTemplate mongoTemplate) {
//            return args -> {
//                Product product = new Product("prod1", "produkt1",true);
//                productRepository.insert(product);
//            };
//
//    }

}
