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
            if (element.contains("-")) {
                String[] split2 = element.split("-");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid range".formatted(cronPart, value));
                }
                int start = CronUtils.parseNumber(cronPart, split2[0]);
                int end = CronUtils.parseNumber(cronPart, split2[1]);
                numbers.addAll(getNumbersInBetween(start, end));
                continue;
            }
            if (element.contains("/")) {
                String[] split2 = element.split("/");
                if (split2.length != 2) {
                    throw new InvalidCronException("[%s] %s is not a valid value".formatted(cronPart, value));
                }
                numbers.addAll(getNumbersSlashed(split2[0], CronUtils.parseNumber(cronPart, split2[1])));
                continue;
            }
            numbers.add(CronUtils.parseNumber(cronPart, element));
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
            start = CronUtils.parseNumber(cronPart, initial);
        }

        for (int i = start; i <= maxValue; i += increment) {
            list.add(i);
        }
        return list;
    }
}
