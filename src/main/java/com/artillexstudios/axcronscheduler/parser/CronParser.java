package com.artillexstudios.axcronscheduler.parser;

import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.impl.DayParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.HourParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.MinuteParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.MonthParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.SecondParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.WeekdayParameter;
import com.artillexstudios.axcronscheduler.scheduler.Schedule;
import org.jetbrains.annotations.Nullable;

import java.util.TimeZone;

public class CronParser {

    public static Schedule createScheduler(String cron) {
//        System.out.println("create scheduler: " + cron);
        return createScheduler(cron, null);
    }

    public static Schedule createScheduler(String cron, @Nullable TimeZone timeZone) {
        String[] split = cron.split(" ");
        Schedule schedule;
        if (split.length == 5) {
            schedule = new Schedule(
                    null,
                    new MinuteParameter(split[0]),
                    new HourParameter(split[1]),
                    new DayParameter(split[2]),
                    new MonthParameter(split[3]),
                    new WeekdayParameter(split[4]),
                    timeZone
            );
        } else if (split.length == 6) {
            schedule = new Schedule(
                    new SecondParameter(split[0]),
                    new MinuteParameter(split[1]),
                    new HourParameter(split[2]),
                    new DayParameter(split[3]),
                    new MonthParameter(split[4]),
                    new WeekdayParameter(split[5]),
                    timeZone
            );
        } else {
            throw new InvalidCronException("invalid cron: " + cron);
        }
        return schedule;
    }
}
