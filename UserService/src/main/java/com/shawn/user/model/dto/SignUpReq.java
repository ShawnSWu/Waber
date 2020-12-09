package com.shawn.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpReq {
    private String email;
    private String password;
    private String name;
    private String carType;
}
