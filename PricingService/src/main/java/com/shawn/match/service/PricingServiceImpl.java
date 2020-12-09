package com.shawn.match.service;

import com.shawn.match.model.dto.ActivityResponse;
import com.shawn.match.model.dto.CarTypeResponse;
import com.shawn.match.model.dto.MatchTripResponse;
import com.shawn.match.model.dto.PricingResultResponse;
import com.shawn.match.model.dto.TripResponse;
import com.shawn.match.pricing.PricingCalculation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PricingServiceImpl implements PricingService {

    private final RestTemplate restTemplate;

    @Value("#{tripServiceUrl}")
    private String TRIP_SERVICE_API;

    @Value("#{matchServiceUrl}")
    private String MATCH_SERVICE_API;

    @Value("#{userServiceUrl}")
    private String USER_SERVICE_API;

    @Value("#{basicPricingCalculator}")
    private PricingCalculation pricingCalculator;

    public PricingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public PricingResultResponse calculateTripPrice(long passengerId, long matchId, long tripId) {
        TripResponse tripResponse = getTrip(tripId, matchId, passengerId);
        MatchTripResponse matchTripResponse = getMatchTrip(matchId, passengerId);
        ActivityResponse activityResponse = getActivity(matchTripResponse.getActivityId());
        CarTypeResponse carTypeResponse = getCarType(matchTripResponse.getCarTypeId());
        int distance = (int)tripResponse.getTripDistance();
        int activityExtraPrice = (int)activityResponse.getExtraPrice();
        int carTypeExtraPrice = (int)carTypeResponse.getExtraPrice();
        int price = pricingCalculator.calculate(distance, carTypeExtraPrice, activityExtraPrice);
        return PricingResultResponse.builder().price(price).build();
    }

    private ActivityResponse getActivity(long activityId) {
        return restTemplate.getForEntity(String.format("%s/api/activity/id/%d", MATCH_SERVICE_API, activityId), ActivityResponse.class).getBody();
    }

    private TripResponse getTrip(long tripId, long matchId, long passengerId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%d/match/%d/trip/%d", TRIP_SERVICE_API, passengerId, matchId, tripId), TripResponse.class).getBody();
    }

    private MatchTripResponse getMatchTrip(long matchId, long passengerId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%d/match/%d", MATCH_SERVICE_API, passengerId, matchId), MatchTripResponse.class).getBody();
    }

    public CarTypeResponse getCarType(long carTypeId) {
        return restTemplate.getForEntity(String.format("%s/api/carType/id/%d", USER_SERVICE_API, carTypeId), CarTypeResponse.class).getBody();
    }
}
