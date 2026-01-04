package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.Parameter;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// *
// JAN-MAY
// 1-12
// JUN,JUL,AUG
// SEP-OCT,MAY-JULY
public class MonthParameter implements Parameter {
    private final Collection<Month> months = new HashSet<>();

    @Override
    public CronPart getCronPart() {
        return CronPart.MONTH;
    }

    public Collection<Month> getMonths() {
        return months;
    }

    public MonthParameter(String value) {
        value = value.replace(" ", "");
        if (value.equals("*")) {
            months.addAll(List.of(Month.values()));
            return;
        }

        // support for MON,TUE
        String[] split = value.split(",");
        for (String element : split) {
            // support for SAT-SUN
            if (element.contains("-")) {
                String[] split2 = element.split("-");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid range".formatted(getCronPart(), value));
                }
                Month start = parseMonth(split2[0]);
                Month end = parseMonth(split2[1]);
                months.addAll(getMonthsInBetween(start, end));
                continue;
            }
            if (element.contains("/")) {
                String[] split2 = element.split("/");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid value".formatted(getCronPart(), value));
                }
                months.addAll(getMonthsSlashed(split2[0], parseNumber(split2[1])));
                continue;
            }
            months.add(parseMonth(element));
        }
    }

    private int parseNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new InvalidCronException("[%s] %s is not a valid integer".formatted(getCronPart(), value));
        }
    }

    private Month parseMonth(String value) {
        value = value.toLowerCase(Locale.ENGLISH);
        return switch (value) {
            case "jan", "january", "1" -> Month.JANUARY;
            case "feb", "february", "2" -> Month.FEBRUARY;
            case "mar", "march", "3" -> Month.MARCH;
            case "apr", "april", "4" -> Month.APRIL;
            case "may", "5" -> Month.MAY;
            case "jun", "june", "6" -> Month.JUNE;
            case "jul", "july", "7" -> Month.JULY;
            case "aug", "august", "8" -> Month.AUGUST;
            case "sep", "september", "9" -> Month.SEPTEMBER;
            case "oct", "october", "10" -> Month.OCTOBER;
            case "nov", "november", "11" -> Month.NOVEMBER;
            case "dec", "december", "12" -> Month.DECEMBER;
            default -> throw new InvalidCronException("[%s] %s is not a valid day of week".formatted(getCronPart(), value));
        };
    }

    private List<Month> getMonthsInBetween(Month start, Month end) {
        List<Month> list = new ArrayList<>();
        while (true) {
            list.add(start);
            if (start.equals(end)) break;
            start = start.plus(1);
        }
        return list;
    }

    private Set<Month> getMonthsSlashed(String initial, int increment) {
        Set<Month> list = new HashSet<>();
        Month start;
        if (initial.equals("*")) {
            start = Month.JANUARY;
        } else {
            start = parseMonth(initial);
        }

        int added = start.getValue() - 1;
        while (true) {
            list.add(start);
            start = start.plus(increment);
            added += increment;
            if (added >= Month.values().length) break;
        }
        return list;
    }
}
