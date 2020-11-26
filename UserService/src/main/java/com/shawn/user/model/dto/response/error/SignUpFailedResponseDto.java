package com.shawn.user.model.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpFailedResponseDto {
    private final String message;
}
