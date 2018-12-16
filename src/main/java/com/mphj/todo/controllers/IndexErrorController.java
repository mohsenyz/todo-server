package com.mphj.todo.controllers;

import com.mphj.todo.entities.User;
import com.mphj.todo.security.auth.filters.RestTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexErrorController implements ErrorController {

    @Autowired
    ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }


    @RequestMapping("/test")
    @Secured(User.Roles.USER_VERIFIED)
    public String test(@AuthenticationPrincipal User user) {
        return "Test " + user.isAuthenticated();
    }

    @RequestMapping("/error")
    public Map<String, Object> error(WebRequest request) {
        Map<String, Object> map = errorAttributes.getErrorAttributes(request, true);
        //map.remove("trace");
        return map;
    }
}
