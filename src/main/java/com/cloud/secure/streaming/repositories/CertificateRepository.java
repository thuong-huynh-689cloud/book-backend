package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Certificate;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface CertificateRepository extends JpaRepository<Certificate, String> {

    List<Certificate> findAllByUserIdOrderByCreatedDateDesc(String userId);

    @Modifying
    @Query("delete from Certificate c where c.id in :certificateIds and c.userId =:userId")
    int deleteByUserId(@Param("certificateIds") List<String> certificateIds,
                       @Param("userId") String userId);

    void deleteByIdIn(List<String> certificateIds);
}
