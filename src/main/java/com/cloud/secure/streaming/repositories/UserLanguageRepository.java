package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.entities.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface UserLanguageRepository extends JpaRepository<UserLanguage, String> {

    List<UserLanguage> findAllByUserIdAndStatus(String userId , AppStatus status);

    void deleteByLanguageInAndUserId(List<Language> language, String userId);

    List<UserLanguage> findAllByUserId(String userId);
}
