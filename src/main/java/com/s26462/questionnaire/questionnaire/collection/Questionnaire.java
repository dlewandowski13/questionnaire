package com.s26462.questionnaire.questionnaire.collection;

import com.s26462.questionnaire.questionnaire.collection.utils.Insurer;
import com.s26462.questionnaire.questionnaire.collection.utils.Question;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "questionnaire")
public class Questionnaire {
    @Id
    private String id;
    private String symbol;
    private String author;
    private String creationDate;
    private Insurer insurer;
    private List<Question> questions;

}
