package com.artillexstudios.axcronscheduler.parameter.impl;

import com.artillexstudios.axcronscheduler.enums.CronPart;
import com.artillexstudios.axcronscheduler.parameter.NumberParameter;
import com.artillexstudios.axcronscheduler.parameter.Parameter;

public class MinuteParameter extends NumberParameter implements Parameter {

    public MinuteParameter(String value) {
        super(CronPart.MINUTE, 0, 59, value);
    }

    @Override
    public CronPart getCronPart() {
        return CronPart.MINUTE;
    }
}
