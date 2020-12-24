package com.shawn.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class SignInReq {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
