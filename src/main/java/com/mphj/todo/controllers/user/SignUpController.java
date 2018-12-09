package com.mphj.todo.controllers.user;


import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.User;
import com.mphj.todo.entities.UserSession;
import com.mphj.todo.models.request.RegisterRequest;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import com.mphj.todo.utils.ErrorUtils;
import com.mphj.todo.utils.Hash;
import com.mphj.todo.utils.Rnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SignUpController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSessionRepository userSessionRepository;

    @PostMapping("/user/register")
    public Object register(@Valid @RequestBody RegisterRequest inputs) {
        if (userRepository.countByEmail(inputs.email.trim()) >= 1) {
            return ErrorUtils.from(500, Constants.Error.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.email = inputs.email;
        user.name = inputs.name;
        user.password = Hash.def(inputs.password);
        user.createdAt = System.currentTimeMillis();
        user.isVerified = false;
        userRepository.save(user);

        Map map = new HashMap();
        map.put("status", HttpStatus.OK.value());
        return map;
    }
}