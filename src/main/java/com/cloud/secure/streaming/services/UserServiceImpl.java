package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldUser;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmailAndStatusAndType(String email, AppStatus status, UserType userType) {
        return userRepository.findByEmailAndStatusAndType(email, status, userType);
    }

    @Override
    public User getByIdAndType(String id, UserType userType) {
        return userRepository.findOneByIdAndTypeAndStatus(id, userType, AppStatus.ACTIVE);
    }

    @Override
    public User getByIdAndStatus(String id, AppStatus status) {
        return userRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public Page<User> getPageUser(String searchKey,
                                  SortFieldUser sortFieldUser,
                                  SortDirection sortDirection, List<UserType> type,
                                  List<AppStatus> statuses,
                                  int pageNumber,
                                  int pageSize) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldUser.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return userRepository.findUsersPage("%" + searchKey + "%", statuses, type, pageable);
    }

    @Override
    public void updateUserByIdInToInactive(List<String> ids) {
        userRepository.updateUserByIdInToInactive(AppStatus.INACTIVE, ids);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email,AppStatus.ACTIVE);
    }

    @Override
    public List<User> getAllByIdIn(List<String> userIds) {
        return userRepository.findAllByIdIn(userIds);
    }

    @Override
    public User getByGoogleIdAndStatus(String googleId, AppStatus status) {
        return userRepository.findByGoogleIdAndStatus(googleId, status);
    }

    @Override
    public User getByFacebookId(String facebookId) {
        return userRepository.findByFacebookIdAndStatus(facebookId,AppStatus.INACTIVE);
    }

    @Override
    public List<User> getAllByTypeInOrderByCreatedDateDesc(List<UserType> userTypes) {
        return userRepository.findAllByTypeInOrderByCreatedDateDesc(userTypes);
    }

    @Override
    public List<User> getAllByCourseIdIn(List<String> courseIds) {
        return userRepository.findAllByCourseIdIn(courseIds);
    }

    @Override
    public List<User> getAllByTypeInAndStatusOrderByCreatedDateDesc(List<UserType> userTypes, AppStatus appStatus){
        return userRepository.findAllByTypeInAndStatusOrderByCreatedDateDesc(userTypes,appStatus);
    }

    @Override
    public List<User> getAllByIdInAndStatus (List<String> ids, AppStatus status){
        return userRepository.findAllByIdInAndStatus(ids, AppStatus.ACTIVE);
    }

    @Override
    public User getUserByCourseId(String courseId) {
        return userRepository.findUserByCourseId(courseId);
    }
}
