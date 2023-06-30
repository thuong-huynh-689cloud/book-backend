package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.UserAdminFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserAdminFeedbackRepository extends JpaRepository<UserAdminFeedback,String> {

    @Modifying
    @Query("DELETE from UserAdminFeedback u where u.userId = :userId")
    void deleteUserAdminFeedbackById(@Param("userId") String userId);
}
