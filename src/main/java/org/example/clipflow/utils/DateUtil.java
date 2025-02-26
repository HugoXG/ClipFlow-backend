package org.example.clipflow.utils;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtil {
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }
}
