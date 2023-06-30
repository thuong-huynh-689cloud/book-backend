package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.config.security.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfoResponse {
    private String id;
    private String name;
    private String email;
    private UserType type;
    private String avatar;

    public AuthInfoResponse(AuthUser authUser) {
        this.id = authUser.getId();
        this.name = authUser.getName();
        this.email = authUser.getEmail();
        this.type = authUser.getType();
        this.avatar = authUser.getAvatar();
    }
}
