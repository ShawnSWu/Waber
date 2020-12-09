package com.shawn.match.pricing;

import org.springframework.stereotype.Component;

@Component("basicPricingCalculator")
public class PricingCalculator implements PricingCalculation {

    private final int BASIC_MILEAGE = 1250;

    private final int BASIC_PRICE = 70;

    private final int continueTripStandardMeter = 200;

    private final int continueTripStandardMeterPrice = 5;

    @Override
    public int calculate(int distance, int carTypeExtraPrice, int activityExtraPrice) {
        int basicPrice = BASIC_PRICE;
        int continueTripMeter = distance - BASIC_MILEAGE;
        if (continueTripMeter > 0) {
            basicPrice += calculateContinueTripPrice(continueTripMeter);
        }
        return basicPrice + carTypeExtraPrice + activityExtraPrice;
    }

    private int calculateContinueTripPrice(int continueTripMeter) {
        int continueTripStandardMeterCount = continueTripMeter / continueTripStandardMeter;
        if ((continueTripMeter % continueTripStandardMeter) == 0) {
            return continueTripStandardMeterCount * continueTripStandardMeterPrice;
        }
        return (continueTripStandardMeterCount + 1) * continueTripStandardMeterPrice;
    }

}
