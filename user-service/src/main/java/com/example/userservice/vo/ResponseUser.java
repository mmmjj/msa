package com.example.userservice.vo;

import lombok.Data;

@Data //ResponseEntity<ResponseUser> 제네릭으로해두면..getter가있어야함
public class ResponseUser {
    private String email;
    private String name;
    private String userId;

}
