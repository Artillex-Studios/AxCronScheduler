package com.artillexstudios.axcronscheduler.parameter;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.utils.CronUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

// *
// 1,2,3,4,5
// 1-5
// */2, 5/2
// 14-22/2
public abstract class NumberParameter {
    protected final Collection<Integer> numbers = new HashSet<>();
    protected final CronPart cronPart;
    protected final int minValue;
    protected final int maxValue;

    public NumberParameter(CronPart cronPart, int minValue, int maxValue, String value) {
        this.cronPart = cronPart;
        this.minValue = minValue;
        this.maxValue = maxValue;
        value = value.replace(" ", "");
        if (value.equals("*")) {
            for (int i = minValue; i <= maxValue; i++) {
                numbers.add(i);
            }
            return;
        }

        String[] split = value.split(",");
        for (String element : split) {
            int slash = 1;
            if (element.contains("/")) {
                String[] split2 = element.split("/");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid value".formatted(cronPart, value));
                }
                element = split2[0];
                slash = CronUtils.parseNumber(cronPart, split2[1]);
            }

            Integer start;
            Integer end = null;
            if (element.contains("-")) {
                String[] split2 = element.split("-");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid range".formatted(cronPart, value));
                }
                start = split2[0].equals("*") ? null : CronUtils.parseNumber(cronPart, split2[0]);
                end = CronUtils.parseNumber(cronPart, split2[1]);
            } else {
                start = element.equals("*") ? null : CronUtils.parseNumber(cronPart, element);
                // support for values like 14/2 (which is every second hour between 14-23)
                if (slash > 1) {
                    end = getMaxValue(cronPart);
                }
            }
            numbers.addAll(getNumbers(start, end, slash));
        }
    }

    // if start is null, that means that all valid numbers should be returned
    // if end is null, that means that the max value should be used
    private List<Integer> getNumbers(Integer start, Integer end, int slash) {
        List<Integer> list = new ArrayList<>();
        if (start == null) {
            start = 0;
            end = getMaxValue(cronPart);
        }
        if (end == null) end = start;
        int idx = 0;
        for (int i = start; i <= end; i++) {
            if (idx++ % slash != 0) continue;
            list.add(i);
        }
        return list;
    }

    private int getMaxValue(CronPart cronPart) {
        return switch (cronPart) {
            case SECOND, MINUTE -> 59;
            case HOUR -> 23;
            default -> throw new InvalidCronException("[%s] not a valid cron part".formatted(cronPart));
        };
    }
}
