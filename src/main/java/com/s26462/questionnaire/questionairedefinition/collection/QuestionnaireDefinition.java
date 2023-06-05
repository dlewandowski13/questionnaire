package com.s26462.questionnaire.questionairedefinition.collection;

import com.s26462.questionnaire.questionairedefinition.collection.utils.Question;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
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
    private Date expiryDate;
    private List<Question> questions;
}
