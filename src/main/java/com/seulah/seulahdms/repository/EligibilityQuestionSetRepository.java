package com.seulah.seulahdms.repository;


import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EligibilityQuestionSetRepository extends JpaRepository<EligibilityQuestionSet, Long> {
    List<EligibilityQuestionSet> findAllById(Long setId);
}
