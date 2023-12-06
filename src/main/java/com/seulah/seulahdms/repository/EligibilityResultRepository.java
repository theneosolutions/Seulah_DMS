package com.seulah.seulahdms.repository;

import com.seulah.seulahdms.entity.EligibilityResult;
import com.seulah.seulahdms.entity.UserVerifiedType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EligibilityResultRepository extends JpaRepository<EligibilityResult, Long> {
    EligibilityResult findByUserId(String  id);

    List<EligibilityResult> findByUserVerifiedType(UserVerifiedType userVerifiedType);
}
