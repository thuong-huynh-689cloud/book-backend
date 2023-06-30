package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateResponse {
    private String accessToken;
    private Long expireTime;

    public AuthenticateResponse(Session session) {
        this.accessToken = session.getId();
        this.expireTime = session.getExpiryDate().getTime()/1000;
    }
}
