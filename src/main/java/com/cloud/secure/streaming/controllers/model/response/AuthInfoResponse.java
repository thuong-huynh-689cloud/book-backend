package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.UserRole;
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
    private UserRole role;

    public AuthInfoResponse(AuthUser authUser) {
        this.id = authUser.getId();
        this.name = authUser.getUsername();
        this.email = authUser.getEmail();
        this.role = authUser.getRole();
    }
}
