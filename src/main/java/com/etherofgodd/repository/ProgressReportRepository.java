package com.etherofgodd.repository;

import com.etherofgodd.domain.ProgressReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProgressReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {}
