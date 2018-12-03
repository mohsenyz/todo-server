package com.mphj.todo.utils;

public class Env {
    public static String get(String key, String value) {
        String var = System.getenv(key);
        if (var == null || var.trim().isEmpty())
            return value;
        return var;
    }
}
