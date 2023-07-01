package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.controllers.model.request.CreateUserRequest;
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
}
