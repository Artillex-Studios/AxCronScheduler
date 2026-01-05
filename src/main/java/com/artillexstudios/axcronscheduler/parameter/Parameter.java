package com.artillexstudios.axcronscheduler.parameter;

import com.artillexstudios.axcronscheduler.enums.CronPart;

import java.time.ZonedDateTime;

public interface Parameter {

    CronPart getCronPart();

    boolean isValid(ZonedDateTime zdt);

    ZonedDateTime nextValid(ZonedDateTime zdt);
}
