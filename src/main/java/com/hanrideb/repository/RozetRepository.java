package com.hanrideb.repository;

import com.hanrideb.domain.Rozet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rozet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RozetRepository extends JpaRepository<Rozet, Long> {}
