package com.hanrideb.repository;

import com.hanrideb.domain.Kayit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Kayit entity.
 */
@Repository
public interface KayitRepository extends JpaRepository<Kayit, Long> {
    @Query(
        value = "select distinct kayit from Kayit kayit left join fetch kayit.dersAnalizleris",
        countQuery = "select count(distinct kayit) from Kayit kayit"
    )
    Page<Kayit> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct kayit from Kayit kayit left join fetch kayit.dersAnalizleris")
    List<Kayit> findAllWithEagerRelationships();

    @Query("select kayit from Kayit kayit left join fetch kayit.dersAnalizleris where kayit.id =:id")
    Optional<Kayit> findOneWithEagerRelationships(@Param("id") Long id);
}
