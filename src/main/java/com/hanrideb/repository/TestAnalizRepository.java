package com.hanrideb.repository;

import com.hanrideb.domain.TestAnaliz;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TestAnaliz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestAnalizRepository extends JpaRepository<TestAnaliz, Long> {
    List<TestAnaliz> findAllByDersAnaliz_AitOldBolum_BolumBaslik(String baslik);

    List<TestAnaliz> findAllByDersAnaliz_Id(Long id);

    boolean existsByTestIdAndDersAnaliz_Id(Long TestId, Long DersAnalizId);
}
