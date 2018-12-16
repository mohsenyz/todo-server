package com.mphj.todo.security.auth;

import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.User;
import com.mphj.todo.entities.UserSession;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    public UserAuthenticationProvider() {

    }

    private static Map<String, UserSession> USERS = new ConcurrentHashMap<>();

    @Autowired
    UserSessionRepository userSessionRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User auth = (User) authentication;
        UserSession userSession = null;
        if (USERS.containsKey(auth.token)) {
            // Use cache
            userSession = USERS.get(auth.token);
        } else {
            userSession = userSessionRepository.findByToken(auth.token).orElse(null);
        }
        if (auth == null || userSession == null) {
            throw new SecurityException(Constants.Error.USER_NOT_FOUND.toString());
        }
        auth.fillFrom(userSession.user);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return User.class.isAssignableFrom(authentication);
    }
}
