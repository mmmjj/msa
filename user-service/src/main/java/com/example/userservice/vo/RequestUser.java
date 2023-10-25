package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    @NotNull(message = "cannot be null")
    @Size(min=2, message = "more")
    @Email
    private String email;

    @NotNull(message = "cannot be null")
    @Size(min=2, message = "more")
    private String name;


    private String pwd;
}
