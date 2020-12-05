package com.shawn.trip.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static Date convertToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date convertToTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date t = null;
        try {
            t = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }
}
