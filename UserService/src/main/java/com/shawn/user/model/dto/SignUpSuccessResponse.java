package com.shawn.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpSuccessResponse {

    private long id;

    private String email;

    private String name;

    private String password;
}
