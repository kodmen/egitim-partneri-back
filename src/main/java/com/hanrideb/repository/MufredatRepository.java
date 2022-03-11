package com.hanrideb.repository;

import com.hanrideb.domain.Mufredat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mufredat entity.
 */
@Repository
public interface MufredatRepository extends JpaRepository<Mufredat, Long> {
    @Query(
        value = "select distinct mufredat from Mufredat mufredat left join fetch mufredat.bolumlers",
        countQuery = "select count(distinct mufredat) from Mufredat mufredat"
    )
    Page<Mufredat> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct mufredat from Mufredat mufredat left join fetch mufredat.bolumlers")
    List<Mufredat> findAllWithEagerRelationships();

    @Query("select mufredat from Mufredat mufredat left join fetch mufredat.bolumlers where mufredat.id =:id")
    Optional<Mufredat> findOneWithEagerRelationships(@Param("id") Long id);
}
