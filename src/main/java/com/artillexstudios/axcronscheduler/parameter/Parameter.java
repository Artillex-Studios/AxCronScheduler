package com.artillexstudios.axcronscheduler.parameter;

import com.artillexstudios.axcronscheduler.enums.CronPart;

public interface Parameter {

    CronPart getCronPart();

//    boolean isValid(Instant instant);
}
