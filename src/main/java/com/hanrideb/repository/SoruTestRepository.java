package com.hanrideb.repository;

import com.hanrideb.domain.SoruTest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoruTest entity.
 */
@Repository
public interface SoruTestRepository extends JpaRepository<SoruTest, Long> {
    @Query(
        value = "select distinct soruTest from SoruTest soruTest left join fetch soruTest.sorulars",
        countQuery = "select count(distinct soruTest) from SoruTest soruTest"
    )
    Page<SoruTest> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct soruTest from SoruTest soruTest left join fetch soruTest.sorulars")
    List<SoruTest> findAllWithEagerRelationships();

    @Query("select soruTest from SoruTest soruTest left join fetch soruTest.sorulars where soruTest.id =:id")
    Optional<SoruTest> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<List<SoruTest>> findAllByTestBolum_BolumBaslik(String b);
}
