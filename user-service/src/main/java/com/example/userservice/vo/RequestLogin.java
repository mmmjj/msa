package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 로그인 정보 입력 객체
 */
@Data
public class RequestLogin {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "cannot")
    @Email
    private String email;
    @NotNull(message = "password cannot be null")
    @Size(min = 8, message = "cannot")
    private String password;
}
