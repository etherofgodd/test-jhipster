package com.etherofgodd.repository;

import com.etherofgodd.domain.Intern;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Intern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {}
