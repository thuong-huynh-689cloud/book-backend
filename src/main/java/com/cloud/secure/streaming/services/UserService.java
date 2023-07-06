package com.cloud.secure.streaming.services;


import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldUser;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User getByEmailUserRole(String email, UserRole userRole);

    User getById(String id);

    User getUserBySessionId(String sessionId);

    User getByEmailAndStatus(String email);

    User saveUser(User user);

    void saveAll(List<User> user);

    List<User> getAllByIdInAndStatus(List<String> ids, Status status);

    void updateUserByIdInToInactive(List<String> ids);

    User getByEmail(String email);

    User getByIdAndStatus(String userId, Status status);

    Page<User> getPageUser(String searchKey, SortFieldUser sortFieldUser, SortDirection sortDirection, List<UserRole> role, List<Status> statuses, int pageNumber, int pageSize);

}
