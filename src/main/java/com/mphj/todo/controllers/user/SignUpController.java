package com.mphj.todo.controllers.user;


import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.User;
import com.mphj.todo.entities.UserSession;
import com.mphj.todo.models.request.RegisterRequest;
import com.mphj.todo.models.response.Response;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import com.mphj.todo.utils.ErrorUtils;
import com.mphj.todo.utils.Hash;
import com.mphj.todo.utils.Rnd;
import com.mphj.todo.utils.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;
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
        user.verificationSentAt = user.createdAt;
        user.verificationCode = Rnd.string(256);
        userRepository.save(user);

        // @TODO send email to user


        return Response.ok();
    }

    @GetMapping(value = "/user/register/{token}/verify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object verifyEmail(@Valid @NotEmpty @Size(min = 128, max = 128) @PathParam("token") String token) {
        User user = userRepository.findByVerificationCode(token).orElse(null);
        if (user == null || !user.verificationCode.equals(token.trim())) {
            return ErrorUtils.from(500, Constants.Error.BAD_VERIFICATION_TOKEN);
        }

        if (Time.diff(user.verificationSentAt) >= Constants.Exp.VERIFICATION_EMAIL) {
            return ErrorUtils.from(500, Constants.Error.VERIFICATION_TOKEN_EXPIRED);
        }

        user.verificationCode = null;
        user.isVerified = true;
        userRepository.save(user);

        return Response.ok();
    }
}