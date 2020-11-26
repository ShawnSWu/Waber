package com.shawn.user.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpSuccessResponseDto {

    private long id;

    private String email;

    private String name;
}
