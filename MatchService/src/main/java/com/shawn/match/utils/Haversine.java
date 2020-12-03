package com.shawn.match.utils;

public class Haversine {

    private static final int EARTH_RADIUS = 6371;

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {
        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);

        return (2 * EARTH_RADIUS * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * 1000;
    }

    private static double haversin(double theta) {
        return Math.pow(Math.sin(theta / 2), 2);
    }

}
