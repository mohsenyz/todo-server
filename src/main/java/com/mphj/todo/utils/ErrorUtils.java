package com.mphj.todo.utils;

import com.mphj.todo.confs.Constants;

import java.util.HashMap;
import java.util.Map;

public class ErrorUtils {

    public static Map from(int errorCode, String message) {
        Map map = new HashMap();
        map.put("status", errorCode);
        map.put("msg", message);
        return map;
    }

    public static Map from(int errorCode, Constants.Error message) {
        return from(errorCode, message.toString());
    }

}
