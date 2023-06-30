package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.Gender;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.entities.UserLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseAndJob {
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
    private List<UserLanguage> userLanguages;
    List<JobExperienceAndTeachingExperienceResponse> jobExperienceAndTeachingExperienceResponses;

    public UserResponseAndJob(User user, List<UserLanguage> userLanguages, List<JobExperienceAndTeachingExperienceResponse> jobExperienceAndTeachingExperienceResponses) {
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
        this.jobExperienceAndTeachingExperienceResponses = jobExperienceAndTeachingExperienceResponses;
    }
}
