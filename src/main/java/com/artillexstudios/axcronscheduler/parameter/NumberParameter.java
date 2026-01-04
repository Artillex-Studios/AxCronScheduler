package com.artillexstudios.axcronscheduler.parameter;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

// *
// 1,2,3,4,5
// 1-5
// */2, 5/2
public abstract class NumberParameter {
    private final Collection<Integer> numbers = new HashSet<>();
    private final CronPart cronPart;
    private final int minValue;
    private final int maxValue;

    public Collection<Integer> getNumbers() {
        return numbers;
    }

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

        // support for MON,TUE
        String[] split = value.split(",");
        for (String element : split) {
            // support for SAT-SUN
            if (element.contains("-")) {
                String[] split2 = element.split("-");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid range".formatted(cronPart, value));
                }
                int start = parseNumber(split2[0]);
                int end = parseNumber(split2[1]);
                numbers.addAll(getNumbersInBetween(start, end));
                continue;
            }
            if (element.contains("/")) {
                String[] split2 = element.split("/");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid value".formatted(cronPart, value));
                }
                numbers.addAll(getNumbersSlashed(split2[0], parseNumber(split2[1])));
                continue;
            }
            numbers.add(parseNumber(element));
        }
    }

    private int parseNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new InvalidCronException("[%s] %s is not a valid integer".formatted(cronPart, value));
        }
    }

    private List<Integer> getNumbersInBetween(int start, int end) {
        List<Integer> list = new ArrayList<>();
        while (true) {
            list.add(start);
            if (start == end) break;
            start++;
        }
        return list;
    }

    private List<Integer> getNumbersSlashed(String initial, int increment) {
        List<Integer> list = new ArrayList<>();
        int start;
        if (initial.equals("*")) {
            start = 0;
        } else {
            start = parseNumber(initial);
        }

        for (int i = start; i <= maxValue; i += increment) {
            list.add(i);
        }
        return list;
    }
}
