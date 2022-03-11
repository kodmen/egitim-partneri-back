package com.hanrideb.repository;

import com.hanrideb.domain.DersAnaliz;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DersAnaliz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DersAnalizRepository extends JpaRepository<DersAnaliz, Long> {}
