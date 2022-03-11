package com.hanrideb.repository;

import com.hanrideb.domain.SiteInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SiteInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteInfoRepository extends JpaRepository<SiteInfo, Long> {}
