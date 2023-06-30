package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.CodeType;
import com.cloud.secure.streaming.entities.Code;
import com.cloud.secure.streaming.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface CodeRepository extends JpaRepository<Code, String> {

    @Modifying
    @Query(value = "INSERT INTO code (id, created_date, expire_date, type, user_id) " +
            "SELECT * FROM (SELECT :id as id, :createdDate as created_date, :expireDate as expire_date, :type as type, :userId as user_id) as temp " +
            "WHERE NOT EXISTS (SELECT user_id FROM reset_code WHERE user_id = :userId AND type = :type )", nativeQuery = true)
    int insertCodeCheckUserId(@Param("id") String id,
                              @Param("createdDate") Date createdDate,
                              @Param("expireDate") Date expireDate,
                              @Param("type") String type,
                              @Param("userId") String userId);

    @Modifying
    @Query("delete from Code where userId =:userId and type = :type")
    void deleteByUserId(@Param("userId") String userId,
                        @Param("type") CodeType type);

    Code findByIdAndType(String resetKey, CodeType type);

    @Modifying
    @Query("delete from Code where id = :id and expireDate >= :date and type = :type")
    int deleteByIdCheckDateAndType(@Param("id") String id,
                                   @Param("date") Date date,
                                   @Param("type") CodeType type);


}
