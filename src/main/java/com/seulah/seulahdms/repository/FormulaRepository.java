package com.seulah.seulahdms.repository;


import com.seulah.seulahdms.entity.Formula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormulaRepository extends JpaRepository<Formula,Long> {
    Formula findByEligibilityQuestionSetId(Long setId);
}
