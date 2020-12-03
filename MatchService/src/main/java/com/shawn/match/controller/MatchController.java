package com.shawn.match.controller;

import com.shawn.match.model.dto.response.MatchedResultDto;
import com.shawn.match.model.dto.response.MatchPreferredConditionDto;
import com.shawn.match.model.dto.response.WaitingMatchDto;
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
    public WaitingMatchDto startingMatch(@RequestBody MatchPreferredConditionDto matchPreferredConditionDto, @PathVariable long passengerId) {
        return matchService.startMatch(matchPreferredConditionDto, passengerId);
    }

    @GetMapping("/users/{passengerId}/match/{matchId}")
    public MatchedResultDto getMatch(@PathVariable long passengerId, @PathVariable long matchId) {
        return matchService.getMatch(passengerId, matchId);
    }
}
