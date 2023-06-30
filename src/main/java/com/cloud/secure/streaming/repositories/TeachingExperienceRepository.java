package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.TeachingExperience;
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
public interface TeachingExperienceRepository extends JpaRepository<TeachingExperience, String> {

    List<TeachingExperience> findAllByJobExperienceIdOrderByCreatedDateDesc(String jobExperienceId);

    void deleteTeachingExperienceByIdIn(List<String> teachingExperienceIds);

    @Modifying
    @Query(value = "DELETE t FROM teaching_experience t,job_experience j WHERE t.id IN :teachingExperienceIds AND t.job_experience_id = j.id AND j.user_id = :userId", nativeQuery = true)
    void deleteTeachingExperienceByIdInAndUserId(@Param("teachingExperienceIds") List<String> teachingExperienceIds,
                                                 @Param("userId") String userId);

    List<TeachingExperience> findAllByIdIn(List<String> ids);

    void deleteAllByJobExperienceId(String jobExperienceId);
}

