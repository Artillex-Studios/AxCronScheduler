package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;
import com.artillexstudios.axcronscheduler.utils.CronUtils;

import java.time.ZonedDateTime;

public class SecondParameter extends NumberParameter implements Parameter {

    public SecondParameter(String value) {
        super(CronPart.SECOND, 0, 59, value);
    }

    @Override
    public CronPart getCronPart() {
        return cronPart;
    }

    @Override
    public boolean isValid(ZonedDateTime zdt) {
        return numbers.contains(zdt.getSecond());
    }

    @Override
    public ZonedDateTime nextValid(ZonedDateTime zdt) {
        for (int i = 0; i < maxValue; i++) {
            zdt = zdt.plusSeconds(1);
            if (isValid(zdt)) {
                return CronUtils.resetTimeUnits(zdt, true, false, false, false, false);
            }
        }
        throw new InvalidCronException("[%s] invalid value".formatted(getCronPart()));
    }
}
