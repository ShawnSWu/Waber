package com.shawn.match.service;

import com.shawn.match.model.dto.response.MatchedResultDto;
import com.shawn.match.model.dto.response.MatchPreferredConditionDto;
import com.shawn.match.model.dto.response.WaitingMatchDto;

public interface MatchService {

    void participateActivity(String activityName, long driverId);

    WaitingMatchDto startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId);

    MatchedResultDto getMatch(long passengerId, long matchId);

}
