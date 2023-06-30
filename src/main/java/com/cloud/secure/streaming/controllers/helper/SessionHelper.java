package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.entities.Session;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.SessionService;
import com.cloud.secure.streaming.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author 689Cloud
 */
@Component
@Slf4j
public class SessionHelper {

    /**
     * Get Auth User from Auth Token (Session ID)
     *
     * @param authToken
     * @param sessionService
     * @param userService
     * @return
     */
    public AuthUser getAuthUserByAuthToken(String authToken, SessionService sessionService, UserService userService) {
        Session session = sessionService.getById(authToken);
        Validator.notNull(session, RestAPIStatus.UNAUTHORIZED, APIStatusMessage.UNAUTHORIZED);
        // Check expired date
        if (DateUtil.convertToUTC(new Date()).getTime() >= session.getExpiryDate().getTime()) {
            throw new ApplicationException(RestAPIStatus.UNAUTHORIZED);
        }
        User user = userService.getById(session.getUserId());
        Validator.notNull(user, RestAPIStatus.UNAUTHORIZED, APIStatusMessage.UNAUTHORIZED);
        return new AuthUser(user, ZoneId.of(session.getZoneId()));
    }

    /**
     * Create new Session
     *
     * @param
     * @param user
     * @return
     */
    public Session createSession(User user, boolean keepLogin, ZoneId zoneId) {
        Session session = new Session();
        // Generate ID
        session.setId(UniqueID.getUUID());
        // Set user id
        session.setUserId(user.getId());
        // Set expired time
        if (keepLogin) {
            // set time keep login 30 days
            session.setExpiryDate(DateUtil.convertToUTC(DateUtil.addDate(new Date(), TimeZone.getDefault(), 30)));
        } else {
            // set time keep login 7 days
            session.setExpiryDate(DateUtil.convertToUTC(DateUtil.addDate(new Date(), TimeZone.getDefault(), 7)));
        }
        if (zoneId != null) {
            session.setZoneId(zoneId.getId());
        }
        return session;
    }
}
