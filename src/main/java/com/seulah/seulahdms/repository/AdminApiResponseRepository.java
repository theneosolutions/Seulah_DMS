package com.seulah.seulahdms.repository;

import com.seulah.seulahdms.entity.AdminApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminApiResponseRepository extends JpaRepository<AdminApiResponse,Long> {
    Optional<AdminApiResponse> findBySetId(Long setId);
}
