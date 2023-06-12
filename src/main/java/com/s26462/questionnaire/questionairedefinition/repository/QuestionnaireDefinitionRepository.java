package com.s26462.questionnaire.questionairedefinition.repository;

import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The interface Questionnaire definition repository.
 */
@Repository
public interface QuestionnaireDefinitionRepository extends MongoRepository<QuestionnaireDefinition, String> {

    /**
     * Find by symbol optional.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     * @return the optional
     */
    @Query("{ symbol: ?0 }")
    Optional<QuestionnaireDefinition> findBySymbol(String questionnaireDefinitionSymbol);

    /**
     * Find custom questionnaire definition list.
     *
     * @param now the now
     * @return the list
     */
    @Query(value = "{$or: [ {expiryDate: {$gt: ?0}}, {expiryDate: null}], publicationDate: {$gt: ?0}}")
    List<Optional<QuestionnaireDefinition>> findCustomQuestionnaireDefinition(Date now);


}
