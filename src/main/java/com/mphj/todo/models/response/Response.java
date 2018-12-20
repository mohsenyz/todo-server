package com.mphj.todo.models.response;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private static final Map OK;

    static {
        OK = new HashMap();
        OK.put("status", 200);
    }

    public static Map ok() {
        return OK;
    }

}
