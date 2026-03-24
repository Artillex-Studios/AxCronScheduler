package com.artillexstudios.axcronscheduler.utils;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class CronUtils {

    public static int getNextLeapYear(TimeZone timeZone) {
        Year year = Year.now(timeZone.toZoneId());
        // the most years possible without a leap year are 8
        for (int i = 0; i < 8; i++) {
            year = year.plusYears(1);
            if (year.isLeap()) return year.getValue();
        }
        throw new InvalidCronException("can't find next leap year around %s".formatted(year.getValue()));
    }

    public static int parseNumber(CronPart cronPart, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new InvalidCronException("[%s] %s is not a valid integer".formatted(cronPart, value));
        }
    }

    // resets the enabled (true) time units to their lowest value
    public static ZonedDateTime resetTimeUnits(ZonedDateTime zdt, boolean nanos, boolean seconds, boolean minutes, boolean hours, boolean days) {
        if (nanos) zdt = zdt.withNano(0);
        if (seconds) zdt = zdt.withSecond(0);
        if (minutes) zdt = zdt.withMinute(0);
        if (hours) zdt = zdt.withHour(0);
        if (days) zdt = zdt.withDayOfMonth(1);
        return zdt;
    }
}
