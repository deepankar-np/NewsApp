package com.deepankar.newsapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class StringUtils {
    public static String getNotNullString(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String getDateTimeString(String dateInString) {
        String pattern;
        if(dateInString.contains(".")){
            dateInString = dateInString.substring(0, dateInString.indexOf("."));
        }
        if(dateInString.endsWith("Z")){
            pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'";
        }else{
            pattern = "yyyy-MM-dd'T'hh:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(dateInString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String datetimeSting = "";
            Calendar today = Calendar.getInstance();
            if (calendar.before(today)) {
                int day = today.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
                if (day > 1) {
                    datetimeSting = day + " days ago";
                } else if (day == 1) {
                    datetimeSting = day + " days ago";
                } else {
                    int hour = today.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
                    if (hour > 1) {
                        datetimeSting = hour + " hours ago";
                    } else if (hour == 1) {
                        datetimeSting = hour + " hour ago";
                    } else {
                        int min = today.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE);
                        if (min > 1) {
                            datetimeSting = min + " mins ago";
                        } else if (min == 1) {
                            datetimeSting = min + " min ago";
                        } else {
                            int sec = today.get(Calendar.SECOND) - calendar.get(Calendar.SECOND);
                            if (sec > 1) {
                                datetimeSting = sec + " secs ago";
                            } else if (min == 1) {
                                datetimeSting = sec + " sec ago";
                            } else {
                                datetimeSting = "now";
                            }
                        }
                    }

                }
            }
            return datetimeSting;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
