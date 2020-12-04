package com.shawn.match.service;

import com.shawn.match.model.dto.response.MatchedResultResponseDto;
import com.shawn.match.model.dto.response.MatchPreferredConditionDto;
import com.shawn.match.model.dto.response.WaitingMatchResponseDto;

public interface MatchService {

    void participateActivity(String activityName, long driverId);

    WaitingMatchResponseDto startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId);

    MatchedResultResponseDto getMatch(long passengerId, String matchId);

}
