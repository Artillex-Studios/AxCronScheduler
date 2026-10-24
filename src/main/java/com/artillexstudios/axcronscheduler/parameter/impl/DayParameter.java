package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;
import com.artillexstudios.axcronscheduler.utils.CronUtils;

import java.time.ZonedDateTime;

public class DayParameter extends NumberParameter implements Parameter {

    public DayParameter(String value) {
        super(CronPart.DAY, 1, 31, value);
    }

    @Override
    public CronPart getCronPart() {
        return cronPart;
    }

    @Override
    public boolean isValid(ZonedDateTime zdt) {
        return numbers.contains(zdt.getDayOfMonth());
    }

    @Override
    public ZonedDateTime nextValid(ZonedDateTime zdt) {
        // todo: don't search impossible years and impossible months
        // for days, we have to use 1461 (365 * 4 + 1) to support leap years
        for (int i = 0; i < 1461; i++) {
            zdt = zdt.plusDays(1);
            if (isValid(zdt)) {
                return CronUtils.resetTimeUnits(zdt, true, true, true, true, false);
            }
        }
        throw new InvalidCronException("[%s] invalid value".formatted(getCronPart()));
    }
}
