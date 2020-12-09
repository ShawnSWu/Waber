package com.shawn.match.controller;

import com.shawn.match.model.dto.PricingResultResponse;
import com.shawn.match.service.PricingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/users/{passengerId}/match/{matchId}/trip/{tripId}/price")
    public PricingResultResponse calculatePricing(@PathVariable long passengerId, @PathVariable long matchId, @PathVariable long tripId) {
        return pricingService.calculateTripPrice(passengerId, matchId, tripId);
    }

}
