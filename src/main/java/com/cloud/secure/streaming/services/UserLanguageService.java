package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.entities.UserLanguage;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface UserLanguageService {

    UserLanguage save(UserLanguage userLanguage);

    void delete(UserLanguage userLanguage);

    UserLanguage getById(String id);

    void saveAll(List<UserLanguage> userLanguages);

    List<UserLanguage> getAllByUserIdAndStatus(String userId, AppStatus status);

    void deleteByLanguageInAndUserId(List<Language> language, String userId);

    List<UserLanguage> getAllByUserId(String userId);

}
