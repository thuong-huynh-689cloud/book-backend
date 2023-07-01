package com.cloud.secure.ecommerce.repositories;

import com.cloud.secure.ecommerce.common.enums.Status;
import com.cloud.secure.ecommerce.common.enums.UserRole;
import com.cloud.secure.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select u from User u where u.status = :status and (u.lastName like :searchKey or u.firstName like :searchKey)")
    Page<User> getByLastNameAndFirstNameContaining(@Param("status") Status status,
                                                   @Param("searchKey") String searchKey,
                                                   Pageable pageable);


    List<User> findAllByIdInAndStatus(List<String> ids, Status status);

    User findByEmail(String email);

}

