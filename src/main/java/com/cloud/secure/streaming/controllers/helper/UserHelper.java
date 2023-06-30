package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.controllers.model.request.CreateSignUpInstructorRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateSignUpLeanerRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateUserRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateUserRequest;
import com.cloud.secure.streaming.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class UserHelper {

    /**
     * createUser
     *
     * @param createUserRequest
     * @param passwordEncoder
     * @return
     */
    public User createUser(CreateUserRequest createUserRequest, PasswordEncoder passwordEncoder, String passwordMD5) {
        User user = new User();
        // add Id to data
        user.setId(UniqueID.getUUID());
        // add email to data
        user.setEmail(createUserRequest.getEmail());
        // add name to data
        user.setName(createUserRequest.getName());
        // add gender to data
        user.setGender(createUserRequest.getGender());
        // add countryCode
        user.setCountryCode(createUserRequest.getCountryCode());
        // add phone to data
        user.setPhone(createUserRequest.getPhone());
        // add avatar to data
        user.setAvatar(createUserRequest.getAvatar());
        // add date to data
        if (createUserRequest.getDob() != null && !createUserRequest.getDob().isEmpty()) {
            try {
                Date date = new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(createUserRequest.getDob());
                user.setDob(date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "Invalid Dob format MM/dd/yyyy");
            }
        }
        // add country to data
        user.setCountry(createUserRequest.getCountry());
        // add phoneDialCode
        user.setPhoneDialCode(createUserRequest.getPhoneDialCode());
        // add city to data
        user.setCity(createUserRequest.getCity());
        // add website to data
        user.setWebsite(createUserRequest.getWebsite());
        // add twitter to data
        user.setTwitter(createUserRequest.getTwitter());
        // add facebook to data
        user.setFacebook(createUserRequest.getFacebook());
        // add linkedin to data
        user.setLinkedin(createUserRequest.getLinkedin());
        // add youtube to data
        user.setYoutube(createUserRequest.getYoutube());
        // add description to data
        user.setDescription(createUserRequest.getDescription());
        //add salt to data
        String newSalt = AppUtil.generateSalt();
        user.setPasswordSalt(newSalt);
        //add password
        user.setPasswordHash(passwordEncoder.encode(passwordMD5.concat(user.getPasswordSalt())));
        //add role
        user.setType(createUserRequest.getType());
        //add status to data
        user.setStatus(AppStatus.ACTIVE);
        // add createDate to data
        user.setCreatedDate(DateUtil.convertToUTC(new Date()));
        // add total point to data
        user.setTotalPoint(0.0);

        return user;
    }

    /**
     * Update User
     *
     * @param user
     * @param updateUserRequest
     * @return
     */
    public User updateUser(User user, UpdateUserRequest updateUserRequest) {

        // Update name
        if (updateUserRequest.getName() != null && !updateUserRequest.getName().trim().isEmpty() &&
                !updateUserRequest.getName().trim().equals(user.getName())) {
            user.setName(updateUserRequest.getName().trim());
        }
        // Update gender
        if (updateUserRequest.getGender() != null &&
                !updateUserRequest.getGender().equals(user.getGender())) {
            user.setGender(updateUserRequest.getGender());
        }
        // Update CountryCode
        if (updateUserRequest.getCountryCode() != null && !updateUserRequest.getCountryCode().trim().isEmpty() &&
                !updateUserRequest.getCountryCode().trim().equals(user.getCountryCode())) {
            user.setCountryCode(updateUserRequest.getCountryCode().trim());
        }
        // Update PhoneDialCode
        if (updateUserRequest.getPhoneDialCode() != null && !updateUserRequest.getPhoneDialCode().trim().isEmpty() &&
                !updateUserRequest.getPhoneDialCode().trim().equals(user.getPhoneDialCode())) {
            user.setPhoneDialCode(updateUserRequest.getPhoneDialCode().trim());
        }
        // Update phone
        if (updateUserRequest.getPhone() != null && !updateUserRequest.getPhone().trim().isEmpty() &&
                !updateUserRequest.getPhone().trim().equals(user.getPhone())) {
            user.setPhone(updateUserRequest.getPhone().trim());
        }
        // Update avatar
        if (updateUserRequest.getAvatar() != null && !updateUserRequest.getAvatar().equals(user.getAvatar())) {
            user.setAvatar(updateUserRequest.getAvatar());
        }
        // Update dob
        if (updateUserRequest.getDob() != null && !updateUserRequest.getDob().trim().isEmpty() &&
                !updateUserRequest.getDob().equals(user.getDob())) {
            try {
                Date date = new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(updateUserRequest.getDob());
                user.setDob(date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "Invalid Dob format MM/dd/yyyy");
            }
        }
        // Update Country
        if (updateUserRequest.getCountry() != null && !updateUserRequest.getCountry().equals(user.getCountry())) {
            user.setCountry(updateUserRequest.getCountry());
        }
        // Update city
        if (updateUserRequest.getCity() != null && !updateUserRequest.getCity().equals(user.getCity())) {
            user.setCity(updateUserRequest.getCity());
        }
        // Update website
        if (updateUserRequest.getWebsite() != null && !updateUserRequest.getWebsite().trim().isEmpty() &&
                !updateUserRequest.getWebsite().trim().equals(user.getWebsite())) {
            user.setWebsite(updateUserRequest.getWebsite().trim());
        }
        // Update Twitter
        if (updateUserRequest.getTwitter() != null && !updateUserRequest.getTwitter().trim().isEmpty() &&
                !updateUserRequest.getTwitter().trim().equals(user.getTwitter())) {
            user.setTwitter(updateUserRequest.getWebsite().trim());
        }
        // Update Facebook
        if (updateUserRequest.getFacebook() != null && !updateUserRequest.getFacebook().trim().isEmpty() &&
                !updateUserRequest.getFacebook().trim().equals(user.getFacebook())) {
            user.setFacebook(updateUserRequest.getFacebook().trim());
        }
        // Update Linkedin
        if (updateUserRequest.getLinkedin() != null && !updateUserRequest.getLinkedin().trim().isEmpty() &&
                !updateUserRequest.getLinkedin().trim().equals(user.getLinkedin())) {
            user.setLinkedin(updateUserRequest.getLinkedin().trim());
        }
        // Update Youtube
        if (updateUserRequest.getYoutube() != null && !updateUserRequest.getYoutube().trim().isEmpty() &&
                !updateUserRequest.getYoutube().trim().equals(user.getYoutube())) {
            user.setYoutube(updateUserRequest.getYoutube().trim());
        }
        // Update Description
        if (updateUserRequest.getDescription() != null && !updateUserRequest.getDescription().trim().isEmpty() &&
                !updateUserRequest.getDescription().trim().equals(user.getDescription())) {
            user.setDescription(updateUserRequest.getDescription().trim());
        }
        return user;
    }

    /**
     * Create Sign Up Instructor
     *
     * @param createSignUpInstructorRequest
     * @param passwordEncoder
     * @param userType
     * @return
     */
    public User createSignUpInstructor(CreateSignUpInstructorRequest createSignUpInstructorRequest, PasswordEncoder passwordEncoder, UserType userType) {
        User user = new User();
        // add Id to data
        user.setId(UniqueID.getUUID());
        // add email to data
        user.setEmail(createSignUpInstructorRequest.getEmail());
        // add name to data
        user.setName(createSignUpInstructorRequest.getName());
        // add gender to data
        user.setGender(createSignUpInstructorRequest.getGender());
        // add phone to data
        user.setPhone(createSignUpInstructorRequest.getPhone());
        // add phoneDialCode
        user.setPhoneDialCode(createSignUpInstructorRequest.getPhoneDialCode());
        // add avatar to data
        user.setAvatar(createSignUpInstructorRequest.getAvatar());
        // add date to data
        if (createSignUpInstructorRequest.getDob() != null && !createSignUpInstructorRequest.getDob().isEmpty()) {
            try {
                Date date = new SimpleDateFormat(Constant.API_FORMAT_TIME).parse(createSignUpInstructorRequest.getDob());
                user.setDob(date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "Invalid Dob format MM/dd/yyyy");
            }
        }
        // add website to data
        user.setWebsite(createSignUpInstructorRequest.getWebsite());
        // add twitter to data
        user.setTwitter(createSignUpInstructorRequest.getTwitter());
        // add facebook to data
        user.setFacebook(createSignUpInstructorRequest.getFacebook());
        // add linkedin to data
        user.setLinkedin(createSignUpInstructorRequest.getLinkedin());
        // add youtube to data
        user.setYoutube(createSignUpInstructorRequest.getYoutube());
        // add description to data
        user.setDescription(createSignUpInstructorRequest.getDescription());
        //add salt to data
        String newSalt = AppUtil.generateSalt();
        user.setPasswordSalt(newSalt);
        //add password
        user.setPasswordHash(passwordEncoder.encode(createSignUpInstructorRequest.getNewPassword().trim().concat(user.getPasswordSalt())));
        //add role
        user.setType(userType);
        //add status to data
        user.setStatus(AppStatus.ACTIVE);
        // add createDate to date
        user.setCreatedDate(DateUtil.convertToUTC(new Date()));
        user.setTotalPoint(0.0);
        return user;
    }

//    /**
//     * Update Sign Up Instructor
//     *
//     * @param user
//     * @param createSignUpInstructorRequest
//     * @param passwordEncoder
//     * @return
//     */
//    public User updateSignUpInstructor(User user, CreateSignUpInstructorRequest createSignUpInstructorRequest, PasswordEncoder passwordEncoder) {
//        // add name to data
//        user.setName(createSignUpInstructorRequest.getName());
//        // add phone to data
//        user.setPhone(createSignUpInstructorRequest.getPhone());
//        // add avatar to data
//        user.setAvatar(createSignUpInstructorRequest.getAvatar());
//        // add date to data
//        if (createSignUpInstructorRequest.getDob() != null && !createSignUpInstructorRequest.getDob().isEmpty()) {
//            try {
//                Date date = new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(createSignUpInstructorRequest.getDob());
//                user.setDob(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "Invalid Dob format MM/dd/yyyy");
//            }
//        }
//        // add website to data
//        user.setWebsite(createSignUpInstructorRequest.getWebsite());
//        // add twitter to data
//        user.setTwitter(createSignUpInstructorRequest.getTwitter());
//        // add facebook to data
//        user.setFacebook(createSignUpInstructorRequest.getFacebook());
//        // add linkedin to data
//        user.setLinkedin(createSignUpInstructorRequest.getLinkedin());
//        // add youtube to data
//        user.setYoutube(createSignUpInstructorRequest.getYoutube());
//        // add description to data
//        user.setDescription(createSignUpInstructorRequest.getDescription());
//        //add salt to data
//        String newSalt = AppUtil.generateSalt();
//        user.setPasswordSalt(newSalt);
//        //add password
//        user.setPasswordHash(passwordEncoder.encode(createSignUpInstructorRequest.getNewPassword().trim().concat(user.getPasswordSalt())));
//        return user;
//    }

    /**
     * Create Sign Up Leaner
     *
     * @param createSignUpLeanerRequest
     * @param passwordEncoder
     * @param userType
     * @return
     */
    public User createSignUpLeaner(CreateSignUpLeanerRequest createSignUpLeanerRequest, PasswordEncoder passwordEncoder,
                                   UserType userType) {
        User user = new User();
        // add Id to data
        user.setId(UniqueID.getUUID());
        // add email to data
        user.setEmail(createSignUpLeanerRequest.getEmail());
        // add name to data
        user.setName(createSignUpLeanerRequest.getName());
        // add gender to data
        user.setGender(createSignUpLeanerRequest.getGender());
        // add countryCode
        user.setCountryCode(createSignUpLeanerRequest.getCountryCode());
        // add phone to data
        user.setPhone(createSignUpLeanerRequest.getPhone());
        // add phoneDialCode
        user.setPhoneDialCode(createSignUpLeanerRequest.getPhoneDialCode());
        // add avatar to data
        user.setAvatar(createSignUpLeanerRequest.getAvatar());

        // add date to data
        if (createSignUpLeanerRequest.getDob() != null && !createSignUpLeanerRequest.getDob().isEmpty()) {
            try {
                Date date = new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(createSignUpLeanerRequest.getDob());
                user.setDob(date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "Invalid Dob format MM/dd/yyyy");
            }
        }
        // add city to data
        user.setCity(createSignUpLeanerRequest.getCity());
        // add description to data
        user.setDescription(createSignUpLeanerRequest.getDescription());
        //add salt to data
        String newSalt = AppUtil.generateSalt();
        user.setPasswordSalt(newSalt);
        //add password
        user.setPasswordHash(passwordEncoder.encode(createSignUpLeanerRequest.getNewPassword().trim().concat(user.getPasswordSalt())));
        //add role
        user.setType(userType);
        //add status to data
        user.setStatus(AppStatus.ACTIVE);
        // add createDate to date
        user.setCreatedDate(DateUtil.convertToUTC(new Date()));
        user.setTotalPoint(0.0);
        return user;
    }

//    /**
//     * Update SignUp Leaner
//     *
//     * @param user
//     * @param createSignUpLeanerRequest
//     * @param passwordEncoder
//     * @return
//     */
//    public User updateSignUpLeaner(User user, CreateSignUpLeanerRequest createSignUpLeanerRequest, PasswordEncoder passwordEncoder) {
//
//        // add name to data
//        user.setName(createSignUpLeanerRequest.getName());
//        // add gender to data
//        user.setGender(createSignUpLeanerRequest.getGender());
//        // add countryCode
//        user.setCountryCode(createSignUpLeanerRequest.getCountryCode());
//        // add phone to data
//        user.setPhone(createSignUpLeanerRequest.getPhone());
//        // add avatar to data
//        user.setAvatar(createSignUpLeanerRequest.getAvatar());
//        // add date to data
//        if (createSignUpLeanerRequest.getDob() != null && !createSignUpLeanerRequest.getDob().isEmpty()) {
//            try {
//                Date date = new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(createSignUpLeanerRequest.getDob());
//                user.setDob(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "Invalid Dob format MM/dd/yyyy");
//            }
//        }
//        // add city to data
//        user.setCity(createSignUpLeanerRequest.getCity());
//        // add description to data
//        user.setDescription(createSignUpLeanerRequest.getDescription());
//        //add salt to data
//        String newSalt = AppUtil.generateSalt();
//        user.setPasswordSalt(newSalt);
//        //add password
//        user.setPasswordHash(passwordEncoder.encode(createSignUpLeanerRequest.getNewPassword().trim().concat(user.getPasswordSalt())));
//
//        return user;
//    }
}
