package com.shawn.match.pricing;

public interface PricingCalculation {

    int calculate(int distance, int carTypeExtraPrice, int activityExtraPrice);
}
