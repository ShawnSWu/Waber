package com.shawn.match.service;

import com.shawn.match.model.dto.PricingResultResponse;

public interface PricingService {

    PricingResultResponse calculateTripPrice(long passengerId, long matchId, long tripId);

}
