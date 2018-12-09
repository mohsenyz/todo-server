package com.mphj.todo.models.request;

import javax.validation.constraints.*;

public class RegisterRequest {

    @NotNull
    @NotEmpty
    public String name;

    @Email
    @NotEmpty
    @NotNull
    public String email;

    @NotNull
    @Size(min = 8, max = 64)
    public String password;

}
