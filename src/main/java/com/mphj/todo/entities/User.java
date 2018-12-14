package com.mphj.todo.entities;

import com.mphj.todo.security.auth.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Collection;

@Entity
public class User implements Authentication {

    public static class Roles {
        public static final String USER_VERIFIED = "user_verified";
    }

    public transient String token;

    public User() {

    }

    public User(String token) {
        this.token = token;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public String name;
    public String email;
    public String password;
    public String verificationCode;
    public long verificationSentAt;
    public long createdAt;
    public boolean isVerified;



    private transient boolean isAuthenticated = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isVerified) {
            return Arrays.asList(
                    Role.from(Roles.USER_VERIFIED)
            );
        } else {
            return null;
        }
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return name;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return name;
    }
}
