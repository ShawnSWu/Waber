package com.shawn.match.service;

import com.shawn.match.model.dto.*;

public interface MatchService {

    StartMatchResponse startMatch(String activityName, String carTypeName, long passengerId);

    MatchedResultResponse getMatch(long passengerId, long matchId);

    void confirmMatched(long matchId, long passengerId);

    void driverCancelMatched(long matchId, long passengerId);

    void passengerCancelMatched(long matchId, long passengerId);

}
