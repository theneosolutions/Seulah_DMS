package com.seulah.seulahdms.repository;


import com.seulah.seulahdms.entity.ScreenName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<ScreenName, Long> {
    ScreenName findByScreenHeading(String screenHeading);

    boolean existsByQuestionIds(Long questionIds);

    List<ScreenName> findByQuestionIds(Long questionIds);

    List<ScreenName> findBySetId(Long setId);
}