package com.seulah.seulahdms.repository;


import com.seulah.seulahdms.entity.ScreenName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<ScreenName, Long> {
    ScreenName findByScreenHeading(String screenHeading);
}