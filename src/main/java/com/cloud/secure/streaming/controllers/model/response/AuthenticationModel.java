package com.cloud.secure.streaming.controllers.model.response;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationModel {

    private String facebookId;
    private String googleId;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;

    public AuthenticationModel(SignUpFbResponse signUpFbResponse) {
        this.facebookId = signUpFbResponse.getId();
        this.googleId = null;
        this.firstName = signUpFbResponse.getFirstName();
        this.lastName = signUpFbResponse.getLastName();
        this.email = signUpFbResponse.getEmail();
//        this.avatar = signUpFbResponse.getPicture();
    }

    public AuthenticationModel(GoogleIdToken.Payload payload) {
        this.facebookId = null;
        this.googleId = payload.getSubject();
        this.firstName = (String) payload.get("given_name");
        this.lastName = (String) payload.get("family_name");
        this.email = payload.getEmail();
    }
}