package com.mphj.todo.security.auth;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private String role;

    private Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public static Role from(String role) {
        return new Role(role);
    }
}
