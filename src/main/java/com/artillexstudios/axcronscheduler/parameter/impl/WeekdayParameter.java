package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.Parameter;
import com.artillexstudios.axcronscheduler.utils.CronUtils;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// *
// MON-SAT
// 0-6 or 1-7
// MON,TUE,FRI
// MON-WED,FRI-SAT
// */2, 2/3
public class WeekdayParameter implements Parameter {
    private final Collection<DayOfWeek> days = new HashSet<>();

    @Override
    public CronPart getCronPart() {
        return CronPart.WEEKDAY;
    }

    public Collection<DayOfWeek> getDays() {
        return days;
    }

    public WeekdayParameter(String value) {
        value = value.replace(" ", "");
        if (value.equals("*")) {
            days.addAll(List.of(DayOfWeek.values()));
            return;
        }

        String[] split = value.split(",");
        for (String element : split) {
            if (element.contains("-")) {
                String[] split2 = element.split("-");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid range".formatted(getCronPart(), value));
                }
                DayOfWeek start = parseDayOfWeek(split2[0]);
                DayOfWeek end = parseDayOfWeek(split2[1]);
                days.addAll(getDaysInBetween(start, end));
                continue;
            }
            if (element.contains("/")) {
                String[] split2 = element.split("/");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid value".formatted(getCronPart(), value));
                }
                days.addAll(getWeekdaysSlashed(split2[0], parseNumber(split2[1])));
                continue;
            }
            days.add(parseDayOfWeek(element));
        }
    }

    private int parseNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new InvalidCronException("[%s] %s is not a valid integer".formatted(getCronPart(), value));
        }
    }

    private DayOfWeek parseDayOfWeek(String value) {
        value = value.toLowerCase(Locale.ENGLISH);
        return switch (value) {
            case "sun", "sunday", "0", "7" -> DayOfWeek.SUNDAY;
            case "mon", "monday", "1" -> DayOfWeek.MONDAY;
            case "tue", "tuesday", "2" -> DayOfWeek.TUESDAY;
            case "wed", "wednesday", "3" -> DayOfWeek.WEDNESDAY;
            case "thu", "thursday", "4" -> DayOfWeek.THURSDAY;
            case "fri", "friday", "5" -> DayOfWeek.FRIDAY;
            case "sat", "saturday", "6" -> DayOfWeek.SATURDAY;
            default -> throw new InvalidCronException("[%s] %s is not a valid day of week".formatted(getCronPart(), value));
        };
    }

    private List<DayOfWeek> getDaysInBetween(DayOfWeek start, DayOfWeek end) {
        List<DayOfWeek> list = new ArrayList<>();
        while (true) {
            list.add(start);
            if (start.equals(end)) break;
            start = start.plus(1);
        }
        return list;
    }

    private Set<DayOfWeek> getWeekdaysSlashed(String initial, int increment) {
        Set<DayOfWeek> list = new HashSet<>();
        DayOfWeek start;
        if (initial.equals("*")) {
            start = DayOfWeek.SUNDAY;
        } else {
            start = parseDayOfWeek(initial);
        }

        int added = getSundayStartValue(start);
        while (true) {
            list.add(start);
            start = start.plus(increment);
            added += increment;
            if (added >= DayOfWeek.values().length) break;
        }
        return list;
    }

    private int getSundayStartValue(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.SUNDAY) return 0;
        return dayOfWeek.getValue();
    }

    @Override
    public boolean isValid(ZonedDateTime zdt) {
        return days.contains(zdt.getDayOfWeek());
    }

    @Override
    public ZonedDateTime nextValid(ZonedDateTime zdt) {
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            zdt = zdt.plusDays(1);
            if (isValid(zdt)) {
                return CronUtils.resetTimeUnits(zdt, true, true, true, true, false);
            }
        }
        throw new InvalidCronException("[%s] invalid value".formatted(getCronPart()));
    }
}
