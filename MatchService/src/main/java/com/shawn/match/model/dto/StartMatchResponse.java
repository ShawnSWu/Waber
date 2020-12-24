package com.shawn.match.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartMatchResponse {
    @JsonProperty("matchId")
    private long matchId;

}
