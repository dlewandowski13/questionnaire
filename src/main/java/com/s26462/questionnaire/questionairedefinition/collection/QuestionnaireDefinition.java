package com.s26462.questionnaire.questionairedefinition.collection;

import com.s26462.questionnaire.questionairedefinition.collection.utils.QuestionDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * The type Questionnaire definition.
 */
@Data
@NoArgsConstructor
@Document(collection = "questionnaireDefinition")
public class QuestionnaireDefinition {

    @Id
    private String id;
    @Indexed(unique = true)
    private String symbol;
    private String author;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date publicationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expiryDate;
    private List<QuestionDefinition> questionDefinitions;
}
