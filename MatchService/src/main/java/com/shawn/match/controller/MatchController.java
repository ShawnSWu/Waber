package com.shawn.match.controller;

import com.shawn.match.model.dto.response.MatchedResultResponseDto;
import com.shawn.match.model.dto.response.MatchPreferredConditionDto;
import com.shawn.match.model.dto.response.WaitingMatchResponseDto;
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
    public WaitingMatchResponseDto startingMatch(@RequestBody MatchPreferredConditionDto matchPreferredConditionDto, @PathVariable long passengerId) {
        return matchService.startMatch(matchPreferredConditionDto, passengerId);
    }

    @GetMapping("/users/{passengerId}/match/{matchId}")
    public MatchedResultResponseDto getMatch(@PathVariable long passengerId, @PathVariable String matchId) {
        return matchService.getMatch(passengerId, matchId);
    }




    @PutMapping("/users/{passengerId}/match/{matchId}/q")
    public MatchedResultResponseDto updateMatchTripStatus(@PathVariable long passengerId, @PathVariable String matchId) {
        return matchService.getMatch(passengerId, matchId);
    }
}
