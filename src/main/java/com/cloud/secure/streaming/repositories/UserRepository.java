package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByEmailAndRoleAndStatus(String email, UserRole role, Status status);

    @Query("select u from User u, Session s where u.id = s.userId and s.id = :sessionId")
    User getUserBySessionId(@RequestParam("sessionId") String sessionId);

    User findByEmailAndStatus(String email, Status status);

    List<User> findAllByIdInAndStatus(List<String> ids, Status status);

    User findByEmail(String email);

    User findByIdAndStatus(String userId , Status status);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("UPDATE User u SET u.status = :status where u.id in :ids ")
    void updateUserByIdInToInactive(@Param("status") Status status,
                                    @Param("ids") List<String> ids);


    @Query("select u from User  u where u.status in (:status) and u.role in (:role) and (u.firstName like :searchKey or u.lastName like :searchKey or u.email like :searchKey)")
    Page<User> findUsersPage(@Param("searchKey") String searchKey,
                             @Param("status") List<Status> status,
                             @Param("role") List<UserRole> role,
                             Pageable pageable);
}

