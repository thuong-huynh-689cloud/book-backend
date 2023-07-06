package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldUser;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getByEmailUserRole(String email, UserRole userRole) {
        return userRepository.findByEmailAndRoleAndStatus(email, userRole, Status.ACTIVE);
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserBySessionId(String sessionId) {
        return userRepository.getUserBySessionId(sessionId);
    }

    @Override
    public User getByEmailAndStatus(String email) {
        return userRepository.findByEmailAndStatus(email, Status.ACTIVE);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void saveAll(List<User> user) {
        userRepository.saveAll(user);
    }

    @Override
    public List<User> getAllByIdInAndStatus(List<String> ids, Status status) {
        return userRepository.findAllByIdInAndStatus(ids, status);
    }

    @Override
    public void updateUserByIdInToInactive(List<String> ids) {
        userRepository.updateUserByIdInToInactive(Status.INACTIVE, ids);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getByIdAndStatus(String userId, Status status) {
        return userRepository.findByIdAndStatus(userId, status);
    }

    @Override
    public Page<User> getPageUser(String searchKey, SortFieldUser sortFieldUser, SortDirection sortDirection, List<UserRole> role, List<Status> statuses, int pageNumber, int pageSize) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldUser.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return userRepository.findUsersPage("%" + searchKey + "%", statuses, role, pageable);
    }
}
