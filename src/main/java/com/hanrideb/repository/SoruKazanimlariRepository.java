package com.hanrideb.repository;

import com.hanrideb.domain.SoruKazanimlari;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoruKazanimlari entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoruKazanimlariRepository extends JpaRepository<SoruKazanimlari, Long> {}
