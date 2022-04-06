package com.hanrideb.repository;

import com.hanrideb.domain.Bolum;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bolum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BolumRepository extends JpaRepository<Bolum, Long> {
    Optional<Bolum> findByBolumBaslik(String bolumName);
}
