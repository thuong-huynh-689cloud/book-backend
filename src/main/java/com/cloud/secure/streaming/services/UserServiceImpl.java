package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.repositories.UserRepository;
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
        return userRepository.findByEmailAndStatus(email,Status.ACTIVE);
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
//
//    @Override
//    public Page<User> getByLastNameAndFirstNameContaining(String name, boolean isAsc, String field, int pageNumber, int pageSize) {
//        String properties = "";
//        switch (field){
//            case "firstName":
//                properties = "firstName";
//                break;
//            case "lastName":
//                properties = "lastName";
//                break;
//            case "status":
//                properties = "status";
//                break;
//            default:
//                properties = "createdDate";
//                break;
//        }
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        Sort sort = Sort.by(direction, properties);
//
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
//
//        return userRepository.getByLastNameAndFirstNameContaining(Status.ACTIVE, "%" + name + "%", pageable);
//
//    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
