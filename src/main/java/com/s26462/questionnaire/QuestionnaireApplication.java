package com.s26462.questionnaire;

import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type Questionnaire application.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({MongoAutoConfiguration.class})
@NoArgsConstructor
public class QuestionnaireApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(QuestionnaireApplication.class, args);
    }

}
