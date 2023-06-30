package com.cloud.secure.streaming.config.security;

import com.cloud.secure.streaming.common.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cloud.secure.streaming.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 689Cloud
 */
public class AuthUser implements UserDetails {

    @Getter
    private final String id;
    private final String username;
    private final String password;
    @Getter
    private String name;
    @Getter
    private String email;
    private final boolean enabled;
    @Getter
    private UserType type;
    @Getter
    private ZoneId zoneId;
    @Getter
    private String avatar;


    public AuthUser(User user, ZoneId zoneId) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = "";
        this.name = user.getName();
        this.email = user.getEmail();
        this.type = user.getType();
        this.zoneId = zoneId;
        this.enabled = true;
        this.avatar = user.getAvatar();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
