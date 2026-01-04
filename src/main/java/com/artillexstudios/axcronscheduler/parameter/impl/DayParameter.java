package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;

public class DayParameter extends NumberParameter implements Parameter {

    public DayParameter(String value) {
        super(CronPart.DAY, 1, 31, value);
    }

    @Override
    public CronPart getCronPart() {
        return CronPart.DAY;
    }
}
