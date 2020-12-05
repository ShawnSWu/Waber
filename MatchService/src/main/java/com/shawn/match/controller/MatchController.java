package com.shawn.match.controller;

import com.shawn.match.model.dto.MatchedResultResponse;
import com.shawn.match.model.dto.MatchPreferredConditionDto;
import com.shawn.match.model.dto.StartMatchResponse;
import com.shawn.match.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/activities/{activityName}/drivers/{driverId}")
    public void driverParticipateActivity(@PathVariable String activityName, @PathVariable long driverId) {
        matchService.participateActivity(activityName, driverId);
    }

    @PostMapping("/users/{passengerId}/match")
    public StartMatchResponse startingMatch(@RequestBody MatchPreferredConditionDto matchPreferredConditionDto, @PathVariable long passengerId) {
        return matchService.startMatch(matchPreferredConditionDto, passengerId);
    }

    @GetMapping("/users/{passengerId}/match/{matchId}")
    public MatchedResultResponse getMatch(@PathVariable long passengerId, @PathVariable long matchId) {
        return matchService.getMatch(passengerId, matchId);
    }

    @PutMapping("/users/{userId}/match/{matchId}/accept")
    public void updateMatchTripStatus(@PathVariable long userId, @PathVariable long matchId) {
        matchService.confirmMatched(matchId, userId);
    }


}
