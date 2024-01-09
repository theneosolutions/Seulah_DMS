package com.seulah.seulahdms.repository;


import com.seulah.seulahdms.entity.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionSetRepository extends JpaRepository<QuestionSet,Long> {
    @Query("SELECT qs FROM QuestionSet qs LEFT JOIN FETCH qs.eligibilityQuestionSet WHERE qs.id = :id")
    Optional<QuestionSet> findByIdWithEligibilityQuestions( Long id);

    List<QuestionSet> findByQuestion(String question);



}
