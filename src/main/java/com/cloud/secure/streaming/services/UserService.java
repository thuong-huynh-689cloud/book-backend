package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldUser;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface UserService {

    User save(User user);

    void delete(User user);

    User getById(String id);

    User getByEmailAndStatusAndType(String email, AppStatus status, UserType userType);

    User getByIdAndType(String id, UserType userType);

    User getByIdAndStatus(String id, AppStatus status);

    Page<User> getPageUser(String searchKey, SortFieldUser sortFieldUser, SortDirection sortDirection, List<UserType> type, List<AppStatus> statuses, int pageNumber, int pageSize);

    void updateUserByIdInToInactive(List<String> ids);

    User getByEmail(String email);

    List<User> getAllByIdIn(List<String> userIds);

    User getByGoogleIdAndStatus(String googleId, AppStatus status);

    User getByFacebookId(String facebookId);

    List<User> getAllByTypeInOrderByCreatedDateDesc(List<UserType> userTypes);

    List<User> getAllByCourseIdIn(List<String> courseIds);

    List<User> getAllByTypeInAndStatusOrderByCreatedDateDesc(List<UserType> userTypes, AppStatus appStatus);

    List<User> getAllByIdInAndStatus(List<String> ids, AppStatus status);

    User getUserByCourseId(String courseId);
}
