package com.mphj.todo.confs;

public class Constants {

    public static class Env {
    }

    public static enum Error {
        EMAIL_ALREADY_EXISTS, BAD_VERIFICATION_TOKEN, VERIFICATION_TOKEN_EXPIRED,
        INCORRECT_PASSWORD, USER_NOT_FOUND, OUTDATED_DATA, BAD_REQUEST, ALREADY_UPDATED
    }

    public static class Exp {
        public static final int VERIFICATION_EMAIL = 60 * 5 * 1000; // 5 Minute
    }



}
