package com.mphj.todo.models.response;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private static final Map OK;

    static {
        OK = new HashMap();
        OK.put("status", HttpStatus.OK);
    }

    public static Map ok() {
        return OK;
    }

}
