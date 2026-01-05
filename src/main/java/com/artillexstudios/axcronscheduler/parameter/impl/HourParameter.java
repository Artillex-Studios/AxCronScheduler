package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;
import com.artillexstudios.axcronscheduler.utils.CronUtils;

import java.time.ZonedDateTime;

public class HourParameter extends NumberParameter implements Parameter {

    public HourParameter(String value) {
        super(CronPart.HOUR, 0, 23, value);
    }

    @Override
    public CronPart getCronPart() {
        return cronPart;
    }

    @Override
    public boolean isValid(ZonedDateTime zdt) {
        return numbers.contains(zdt.getHour());
    }

    @Override
    public ZonedDateTime nextValid(ZonedDateTime zdt) {
        for (int i = 0; i < maxValue; i++) {
            zdt = zdt.plusHours(1);
            if (isValid(zdt)) {
                return CronUtils.resetTimeUnits(zdt, true, true, true, false, false);
            }
        }
        throw new InvalidCronException("[%s] invalid value".formatted(getCronPart()));
    }
}
