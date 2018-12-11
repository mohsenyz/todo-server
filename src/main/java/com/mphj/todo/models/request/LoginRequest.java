package com.mphj.todo.models.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class LoginRequest {

    @NotEmpty
    @Email
    public String email;

    @NotEmpty
    public String password;

    @NotEmpty
    public String imei;

}
