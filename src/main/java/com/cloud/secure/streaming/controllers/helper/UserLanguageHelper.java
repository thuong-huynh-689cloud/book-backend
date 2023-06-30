package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.UpdateUserLanguageRequest;
import com.cloud.secure.streaming.entities.UserLanguage;
import com.cloud.secure.streaming.services.UserLanguageService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 689Cloud
 */
@Component
public class UserLanguageHelper {
//    final UserLanguageService userLanguageService;
//
//    public UserLanguageHelper(UserLanguageService userLanguageService) {
//        this.userLanguageService = userLanguageService;
//    }

    /**
     * Create User Language
     *
     * @param userId
     * @param language
     * @return
     */
    private UserLanguage createUserLanguage(String userId, Language language) {

        UserLanguage userLanguage = new UserLanguage();
        userLanguage.setId(UniqueID.getUUID());
        userLanguage.setLanguage(language);
        userLanguage.setUserId(userId);
        userLanguage.setStatus(AppStatus.ACTIVE);
        userLanguage.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return userLanguage;
    }

    public List<UserLanguage> createUserLanguages(String userId, List<Language> languages) {
        return languages.stream().distinct().map(language -> createUserLanguage(userId, language)).collect(Collectors.toList());
    }
}
