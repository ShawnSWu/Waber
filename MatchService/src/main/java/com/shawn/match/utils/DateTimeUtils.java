package com.shawn.match.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    public static String getCurrentTime(DateTimeFormatter dateTimeFormatter){
        return LocalTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentDate(DateTimeFormatter dateTimeFormatter){
        return LocalDate.now().format(dateTimeFormatter);
    }

    public static Date getCurrentTime(){
        String time = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date t = null;
        try {
            t = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static String convertDateToText(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String convertTimeToText(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(time);
    }
}
