package com.seulah.seulahdms.repository;


import com.seulah.seulahdms.entity.EligibilityQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EligibilityQuestionsRepository extends JpaRepository<EligibilityQuestions, Long> {
    void deleteByQuestion(String question);

    @Transactional
    EligibilityQuestions findByQuestion(String question);

    EligibilityQuestions findByHeadingOrQuestion(String heading, String question);
}
