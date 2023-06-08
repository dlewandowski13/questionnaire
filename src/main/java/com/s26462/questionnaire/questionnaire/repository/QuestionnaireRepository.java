package com.s26462.questionnaire.questionnaire.repository;

import com.s26462.questionnaire.questionnaire.collection.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Questionnaire repository.
 */
@Repository
public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {
}
