package com.mphj.todo.controllers.user;

import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.User;
import com.mphj.todo.entities.UserSession;
import com.mphj.todo.models.request.LoginRequest;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import com.mphj.todo.utils.ErrorUtils;
import com.mphj.todo.utils.Hash;
import com.mphj.todo.utils.Rnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSessionRepository userSessionRepository;

    @PostMapping("/user/login")
    public Object login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email);
        if (user == null) {
            return ErrorUtils.from(500, Constants.Error.USER_NOT_FOUND);
        }
        if (!Hash.validate(loginRequest.password, user.password)) {
            return ErrorUtils.from(403, Constants.Error.INCORRECT_PASSWORD);
        }

        String prevToken = userSessionRepository.findTokenByIdAndIMEI(user.id, loginRequest.imei.trim());
        if (prevToken == null) {
            UserSession userSession = new UserSession();
            userSession.user = user;
            userSession.createdAt = System.currentTimeMillis();
            userSession.imei = loginRequest.imei.trim();
            userSession.lastSeen = userSession.createdAt;
            userSession.token = Rnd.string(255);
            userSessionRepository.save(userSession);
            prevToken = userSession.token;
        }

        Map map = new HashMap();
        map.put("status", HttpStatus.OK);
        map.put("token", prevToken);
        return map;
    }

}
