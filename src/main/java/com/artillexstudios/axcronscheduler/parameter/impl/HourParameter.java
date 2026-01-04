package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;

public class HourParameter extends NumberParameter implements Parameter {

    public HourParameter(String value) {
        super(CronPart.HOUR, 0, 23, value);
    }

    @Override
    public CronPart getCronPart() {
        return CronPart.HOUR;
    }
}
