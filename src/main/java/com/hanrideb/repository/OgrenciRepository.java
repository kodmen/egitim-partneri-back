package com.hanrideb.repository;

import com.hanrideb.domain.Ogrenci;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ogrenci entity.
 */
@Repository
public interface OgrenciRepository extends JpaRepository<Ogrenci, Long> {
    @Query(
        value = "select distinct ogrenci from Ogrenci ogrenci left join fetch ogrenci.rozetlers",
        countQuery = "select count(distinct ogrenci) from Ogrenci ogrenci"
    )
    Page<Ogrenci> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct ogrenci from Ogrenci ogrenci left join fetch ogrenci.rozetlers")
    List<Ogrenci> findAllWithEagerRelationships();

    @Query("select ogrenci from Ogrenci ogrenci left join fetch ogrenci.rozetlers where ogrenci.id =:id")
    Optional<Ogrenci> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Ogrenci> findByStudentUser_Id(Long id);
}
