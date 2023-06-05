package com.s26462.questionnaire.questionairedefinition.repository;

import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionnaireDefinitionRepository extends MongoRepository<QuestionnaireDefinition, String> {

    @Query("{ symbol: ?0 }")
    Optional<QuestionnaireDefinition> findBySymbol(String questionnaireDefinitionSymbol);

    @Query(value = "{$or: [ {expiryDate: {$gt: ?0}}, {expiryDate: null}], publicationDate: {$gt: ?0}}")
    List<Optional<QuestionnaireDefinition>> findCustomQuestionnaireDefinition(Date now);


}
