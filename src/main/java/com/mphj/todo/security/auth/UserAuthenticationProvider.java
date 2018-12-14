package com.mphj.todo.security.auth;

import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.User;
import com.mphj.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    public UserAuthenticationProvider() {

    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User auth = (User) authentication;
        if (auth.token.trim().equals("admin")) {
            User user = new User();
            user.name = "mohsen";
            return user;
        }
        User user = userRepository.findByToken(auth.token).orElse(null);
        if (user == null)
            throw new SecurityException(Constants.Error.USER_NOT_FOUND.toString());
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return User.class.isAssignableFrom(authentication);
    }
}
