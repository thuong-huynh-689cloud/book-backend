package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 689Cloud
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmailAndStatusAndType(String email, AppStatus status, UserType userType);
}
