package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.JobExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface JobExperienceRepository extends JpaRepository<JobExperience, String> {


    List<JobExperience> findAllByUserIdOrderByCreatedDateDesc(String userId);

    @Modifying
    @Query("DELETE from JobExperience j where j.id in :ids and j.userId =:userId")
    void deleteByUserId(@Param("ids") List<String> jobExperienceIds, String userId);

    void deleteByIdIn(List<String> jobExperienceIds);

    List<JobExperience> findAllByIdIn(List<String> ids);

    List<JobExperience> findAllByUserId(String userId);

}
