package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmailAndStatusAndType(String email, AppStatus status, UserType userType);

    User findOneByIdAndTypeAndStatus(String id, UserType userType, AppStatus status);

    User findByIdAndStatus(String id, AppStatus status);

    @Query("select u from User  u where u.status in (:status) and u.type in (:type) and (u.name like :searchKey or u.email like :searchKey)")
    Page<User> findUsersPage(@Param("searchKey") String searchKey,
                             @Param("status") List<AppStatus> statuses,
                             @Param("type") List<UserType> type,
                             Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status where u.id in :ids ")
    void updateUserByIdInToInactive(@Param("status") AppStatus status,
                                    @Param("ids") List<String> ids);

    List<User> findAllByIdIn(List<String> userIds);

    User findByEmailAndStatus(String email, AppStatus appStatus);

    User findByGoogleIdAndStatus(String googleId, AppStatus status);

    User findByFacebookIdAndStatus(String facebookId, AppStatus status);

    List<User> findAllByTypeInOrderByCreatedDateDesc(List<UserType> userTypes);

    @Query("select u from User u, Course c where c.userId = u.id and c.id in :courseIds")
    List<User> findAllByCourseIdIn(@Param("courseIds") List<String> courseIds);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET total_point = total_point - :deductedPoint WHERE " +
            "(total_point - :deductedPoint) >= 0.0 and id = :id LIMIT 1;", nativeQuery = true)
    int updateUserPointById(@Param("deductedPoint") double point,
                            @Param("id") String id);

    List<User> findAllByTypeInAndStatusOrderByCreatedDateDesc(List<UserType> userTypes, AppStatus status);

    @Query("select u from User u where u.type in :userTypes and u.status = :appStatus order by u.createdDate")
    List<User> findAllByTypeIn(List<UserType> userTypes, AppStatus appStatus);

    List<User> findAllByIdInAndStatus(List<String> ids, AppStatus appStatus);

    @Query("select u from User u , Course  c  where u.id = c.userId and c.id = :courseId")
    User findUserByCourseId (@Param("courseId") String courseId);
}
