package com.hanrideb.repository;

import com.hanrideb.domain.Soru;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Soru entity.
 */
@Repository
public interface SoruRepository extends JpaRepository<Soru, Long> {
    @Query(
        value = "select distinct soru from Soru soru left join fetch soru.kazanimlars",
        countQuery = "select count(distinct soru) from Soru soru"
    )
    Page<Soru> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct soru from Soru soru left join fetch soru.kazanimlars")
    List<Soru> findAllWithEagerRelationships();

    @Query("select soru from Soru soru left join fetch soru.kazanimlars where soru.id =:id")
    Optional<Soru> findOneWithEagerRelationships(@Param("id") Long id);
}
