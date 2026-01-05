package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;
import com.artillexstudios.axcronscheduler.utils.CronUtils;

import java.time.ZonedDateTime;

public class MinuteParameter extends NumberParameter implements Parameter {

    public MinuteParameter(String value) {
        super(CronPart.MINUTE, 0, 59, value);
    }

    @Override
    public CronPart getCronPart() {
        return cronPart;
    }

    @Override
    public boolean isValid(ZonedDateTime zdt) {
        return numbers.contains(zdt.getMinute());
    }

    @Override
    public ZonedDateTime nextValid(ZonedDateTime zdt) {
        for (int i = 0; i < maxValue; i++) {
            zdt = zdt.plusMinutes(1);
            if (isValid(zdt)) {
                return CronUtils.resetTimeUnits(zdt, true, true, false, false, false);
            }
        }
        throw new InvalidCronException("[%s] invalid value".formatted(getCronPart()));
    }
}
