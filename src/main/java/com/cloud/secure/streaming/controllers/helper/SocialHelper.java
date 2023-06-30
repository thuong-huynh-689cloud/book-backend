package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.controllers.model.response.AuthenticationModel;
import com.cloud.secure.streaming.controllers.model.response.SignUpFbResponse;
import com.cloud.secure.streaming.entities.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author 689Cloud
 */
@Slf4j
@Component
public class SocialHelper {
    public AuthenticationModel createAuthModelGoogle(String tokenId, String clientId) {
        GoogleIdToken googleIdToken;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, new JacksonFactory())
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList(clientId))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();
            // (Receive idTokenString by HTTPS POST)
            googleIdToken = verifier.verify(tokenId);
        } catch (Exception e) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid google token");
        }
        if (googleIdToken == null) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid google token");
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        return new AuthenticationModel(payload);
    }

    public AuthenticationModel createAuthModelFB(String tokenId, String clientId) {

        log.error("tokenId: " + tokenId);
        Facebook facebook = new FacebookTemplate(tokenId);

        System.out.println(facebook);
        String[] fields = {"id", "first_name", "last_name", "email"};
        // get data from facebook
        SignUpFbResponse signUpFbResponse;
        try {
            signUpFbResponse = facebook.fetchObject("me", SignUpFbResponse.class, fields);
            log.info(signUpFbResponse.toString());
        } catch (InvalidAuthorizationException e) {
            log.error(e.getMessage());
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid facebook token");
        }
        if (signUpFbResponse == null) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid facebook token");
        }
        return new AuthenticationModel(signUpFbResponse);
    }

    public User mappingGoogleInfoWith(AuthenticationModel authenticationModel) {
        //Create new user
        User user = new User();
        user.setGoogleId(authenticationModel.getGoogleId());
        Validator.validateEmailFormat(authenticationModel.getEmail());
        user.setEmail(authenticationModel.getEmail());
        user.setStatus(AppStatus.ACTIVE);
        user.setName(authenticationModel.getFirstName() + " " + authenticationModel.getLastName());

        return user;
    }

    public User mappingFacebookInfoWith(AuthenticationModel authenticationModel) {
        //Create new user
        log.info(authenticationModel.toString());
        User user = new User();
        user.setId(UniqueID.getUUID());
        user.setFacebookId(authenticationModel.getFacebookId());
        if (authenticationModel.getEmail() != null && !authenticationModel.getEmail().trim().isEmpty()) {
            Validator.validateEmailFormat(authenticationModel.getEmail());
            user.setEmail(authenticationModel.getEmail());
        }
        user.setStatus(AppStatus.ACTIVE);
        user.setName(authenticationModel.getFirstName() + " " + authenticationModel.getLastName());

        return user;
    }
}
