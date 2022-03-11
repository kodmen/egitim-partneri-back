package com.hanrideb.repository;

import com.hanrideb.domain.Yorum;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Yorum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface YorumRepository extends JpaRepository<Yorum, Long> {
    @Query("select yorum from Yorum yorum where yorum.userYorum.login = ?#{principal.username}")
    List<Yorum> findByUserYorumIsCurrentUser();
}
