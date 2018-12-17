package com.mphj.todo.controllers.user;


import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.User;
import com.mphj.todo.models.request.RegisterRequest;
import com.mphj.todo.models.response.Response;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import com.mphj.todo.utils.ErrorUtils;
import com.mphj.todo.utils.Hash;
import com.mphj.todo.utils.Rnd;
import com.mphj.todo.utils.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        user.verificationCode = Rnd.string(255);
        userRepository.save(user);

        // @TODO send email to user


        return Response.ok();
    }

    @GetMapping(value = "/user/register/verify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByVerificationCode(token).orElse(null);
        System.out.println("TAG :: " + token);
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