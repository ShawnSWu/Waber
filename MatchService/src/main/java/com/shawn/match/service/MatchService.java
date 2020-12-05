package com.shawn.match.service;

import com.shawn.match.model.dto.MatchedResultResponse;
import com.shawn.match.model.dto.MatchPreferredConditionDto;
import com.shawn.match.model.dto.StartMatchResponse;

public interface MatchService {

    void participateActivity(String activityName, long driverId);

    StartMatchResponse startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId);

    MatchedResultResponse getMatch(long passengerId, long matchId);

    void confirmMatched(long matchId, long passengerId);

    void driverCancelMatched(long matchId, long passengerId);

    void passengerCancelMatched(long matchId, long passengerId);

//    MatchTripDto getMatchTrip(String matchId);

}
