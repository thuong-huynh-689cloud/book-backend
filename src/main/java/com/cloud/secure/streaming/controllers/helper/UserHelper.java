package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.controllers.model.request.CreateSignUpRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateUserRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateUserRequest;
import com.cloud.secure.streaming.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserHelper {
    public User createUser(CreateUserRequest createUserRequest, PasswordEncoder passwordEncoder) {
        User user = new User();
        //add id to data
        user.setId(UniqueID.getUUID());
        //add id to role
        user.setRole(createUserRequest.getRole());
        //add email to data
        user.setEmail(createUserRequest.getEmail());
        //add fist name to data
        user.setFirstName(createUserRequest.getFistName());
        //add last name to data
        user.setLastName(createUserRequest.getLastName());
        //add status to data
        user.setStatus(Status.ACTIVE);
        //add date to data
        user.setCreatedDate(DateUtil.convertToUTC(new Date()));
        //add salt to data
        String newSalt = AppUtil.generateSalt();
        user.setPasswordSalt(newSalt);
        //add password
        if (createUserRequest.getNewPassword().equals(createUserRequest.getConfirmNewPassword())) {
            // encode password
            user.setPasswordHash(passwordEncoder.encode(createUserRequest.getNewPassword().trim().concat(user.getPasswordSalt())));
        } else {
            throw new ApplicationException(RestAPIStatus.FAIL, "password incorrect, please try again");
        }
        //add address to data
        user.setAddress(createUserRequest.getAddress());
        // add phoneNumber to data
        user.setPhoneNumber(createUserRequest.getPhoneNumber());

        return user;
    }

    public User updateUser(User user, UpdateUserRequest updateUserRequest, PasswordEncoder passwordEncoder) {

        // check fist name
        if (updateUserRequest.getFirstName() != null && !updateUserRequest.getFirstName().trim().isEmpty() &&
                !updateUserRequest.getFirstName().trim().equals(user.getFirstName())) {
            user.setFirstName(updateUserRequest.getFirstName().trim());
        }
        //check last name
        if (updateUserRequest.getLastName() != null && !updateUserRequest.getLastName().trim().isEmpty() &&
                !updateUserRequest.getLastName().trim().equals(user.getLastName())) {
            user.setLastName(updateUserRequest.getLastName().trim());
        }

        if (updateUserRequest.getAddress() != null && !updateUserRequest.getAddress().trim().isEmpty() &&
                !updateUserRequest.getAddress().trim().equals(user.getAddress())) {
            user.setAddress(updateUserRequest.getAddress());
        }
        // validate phone number
        if (updateUserRequest.getPhoneNumber() != null && !updateUserRequest.getPhoneNumber().isEmpty() &&
                !updateUserRequest.getPhoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(updateUserRequest.getPhoneNumber());
        }

        return user;
    }

//    public User createSignUp(CreateSignUpRequest createSignUpRequest, PasswordEncoder passwordEncoder) {
//        User user = new User();
//        user.setId(UniqueID.getUUID());
//        user.setEmail(createSignUpRequest.getEmail());
//        String newSalt = AppUtil.generateSalt();
//        user.setSalt(newSalt);
//        user.setPasswordHash(passwordEncoder.encode(createSignUpRequest.getPasswordHash().trim().concat(user.getSalt())));
//        user.setRole(UserRole.CUSTOMER);
//        user.setStatus(Status.PENDING);
//
//        return user;
//    }
}

