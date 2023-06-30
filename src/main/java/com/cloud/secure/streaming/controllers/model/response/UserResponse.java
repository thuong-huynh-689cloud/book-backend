package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.common.enums.Gender;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.entities.UserAdminFeedback;
import com.cloud.secure.streaming.entities.UserLanguage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;
    private String email;
    private String name;
    private Gender gender; // MALE, FEMALE , OTHER
    private String countryCode; // country phone number code
    private String phoneDialCode;
    private String phone;
    private String avatar;
    private Date dob;
    private String country;
    private String city;
    private String website;
    private String twitter;
    private String facebook;
    private String linkedin;
    private String youtube;
    private String description;
    private UserType type;
    private AppStatus status;
    private Date createdDate;
    private Double totalPoint;
    private ZoneId zoneId;
    private List<UserLanguage> userLanguages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserAdminFeedback userAdminFeedback;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long totalUserEnrol;

    private String cardNumber; // stripe

    private String cardHolder; // stripe

    private String cardExpDate; // stripe

    private CardType cardType; // stripe

    public UserResponse(User user, List<UserLanguage> userLanguages) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.gender = user.getGender();
        this.countryCode = user.getCountryCode();
        this.phoneDialCode = user.getPhoneDialCode();
        this.phone = user.getPhone();
        this.avatar = user.getAvatar();
        this.dob = user.getDob();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.website = user.getWebsite();
        this.twitter = user.getTwitter();
        this.facebook = user.getFacebook();
        this.linkedin = user.getLinkedin();
        this.youtube = user.getYoutube();
        this.description = user.getDescription();
        this.type = user.getType();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.totalPoint = user.getTotalPoint();
        this.userLanguages = userLanguages;
    }

    public UserResponse(User user, List<UserLanguage> userLanguages ,Long totalUserEnrol) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.gender = user.getGender();
        this.countryCode = user.getCountryCode();
        this.phoneDialCode = user.getPhoneDialCode();
        this.phone = user.getPhone();
        this.avatar = user.getAvatar();
        this.dob = user.getDob();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.website = user.getWebsite();
        this.twitter = user.getTwitter();
        this.facebook = user.getFacebook();
        this.linkedin = user.getLinkedin();
        this.youtube = user.getYoutube();
        this.description = user.getDescription();
        this.type = user.getType();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.totalPoint = user.getTotalPoint();
        this.userLanguages = userLanguages;
        this.totalUserEnrol = totalUserEnrol;
    }


    public UserResponse(User user, UserAdminFeedback userAdminFeedback) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.gender = user.getGender();
        this.countryCode = user.getCountryCode();
        this.phoneDialCode = user.getPhoneDialCode();
        this.phone = user.getPhone();
        this.avatar = user.getAvatar();
        this.dob = user.getDob();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.website = user.getWebsite();
        this.twitter = user.getTwitter();
        this.facebook = user.getFacebook();
        this.linkedin = user.getLinkedin();
        this.youtube = user.getYoutube();
        this.description = user.getDescription();
        this.type = user.getType();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.totalPoint = user.getTotalPoint();
        this.userAdminFeedback = userAdminFeedback;
    }
}
