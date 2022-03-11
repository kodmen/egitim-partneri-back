package com.hanrideb.repository;

import com.hanrideb.domain.Ogretmen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ogretmen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OgretmenRepository extends JpaRepository<Ogretmen, Long> {}
