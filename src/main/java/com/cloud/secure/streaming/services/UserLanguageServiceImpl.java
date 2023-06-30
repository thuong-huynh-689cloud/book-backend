package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.entities.UserLanguage;
import com.cloud.secure.streaming.repositories.UserLanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class UserLanguageServiceImpl implements UserLanguageService{

    final UserLanguageRepository userLanguageRepository;

    public UserLanguageServiceImpl(UserLanguageRepository userLanguageRepository) {
        this.userLanguageRepository = userLanguageRepository;
    }

    @Override
    public UserLanguage save(UserLanguage userLanguage) {
        return userLanguageRepository.save(userLanguage);
    }

    @Override
    public void delete(UserLanguage userLanguage) {
        userLanguageRepository.delete(userLanguage);
    }

    @Override
    public UserLanguage getById(String id) {
        return userLanguageRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAll(List<UserLanguage> userLanguages) {
        userLanguageRepository.saveAll(userLanguages);
    }

    @Override
    public List<UserLanguage> getAllByUserIdAndStatus(String userId, AppStatus status) {
        return userLanguageRepository.findAllByUserIdAndStatus(userId,status);
    }


    @Override
    public void deleteByLanguageInAndUserId(List<Language> language, String userId) {
        userLanguageRepository.deleteByLanguageInAndUserId(language, userId);
    }

    @Override
    public List<UserLanguage> getAllByUserId(String userId) {
        return userLanguageRepository.findAllByUserId(userId);
    }
}

