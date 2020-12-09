package com.shawn.match.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipateActivityFailedResponse {
    @JsonProperty("message")
    private final String message;
}
