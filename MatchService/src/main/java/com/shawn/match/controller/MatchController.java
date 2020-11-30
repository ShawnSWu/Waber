package com.shawn.match.controller;


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
}
