package com.hanrideb.repository;

import com.hanrideb.domain.ImageModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ImageModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
    ImageModel findByName(String name);
}
