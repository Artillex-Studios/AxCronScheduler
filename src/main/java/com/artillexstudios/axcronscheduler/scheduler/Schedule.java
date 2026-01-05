package com.artillexstudios.axcronscheduler.scheduler;

import com.artillexstudios.axcronscheduler.exception.InvalidCronException;
import com.artillexstudios.axcronscheduler.parameter.Parameter;
import com.artillexstudios.axcronscheduler.parameter.impl.DayParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.HourParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.MinuteParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.MonthParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.SecondParameter;
import com.artillexstudios.axcronscheduler.parameter.impl.WeekdayParameter;
import com.artillexstudios.axcronscheduler.utils.CronUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Schedule {
    private final SecondParameter secondParameter;
    private final MinuteParameter minuteParameter;
    private final HourParameter hourParameter;
    private final DayParameter dayParameter;
    private final MonthParameter monthParameter;
    private final WeekdayParameter weekdayParameter;
    private TimeZone timeZone;
    private ZonedDateTime zdt;
    private List<Parameter> parameters = new ArrayList<>();

    public Schedule(@Nullable SecondParameter secondParameter,
                    @NotNull MinuteParameter minuteParameter,
                    @NotNull HourParameter hourParameter,
                    @NotNull DayParameter dayParameter,
                    @NotNull MonthParameter monthParameter,
                    @NotNull WeekdayParameter weekdayParameter,
                    @Nullable TimeZone timeZone
    ) {
        this.secondParameter = secondParameter;
        this.minuteParameter = minuteParameter;
        this.hourParameter = hourParameter;
        this.dayParameter = dayParameter;
        this.monthParameter = monthParameter;
        this.weekdayParameter = weekdayParameter;
        if (secondParameter != null) parameters.add(secondParameter);
        // add parameters from longest to shortest period to (save time)
        parameters.add(monthParameter);
        parameters.add(dayParameter);
        parameters.add(weekdayParameter);
        parameters.add(hourParameter);
        parameters.add(minuteParameter);
        this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
        calculateNextRunTime();
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    private void calculateNextRunTime() {
        // set value to current time
        zdt = ZonedDateTime.now(timeZone.toZoneId());
        // get next second or minute (depending on mode used)
        zdt = zdt.plusNanos(1000000000 - zdt.getNano());
        if (secondParameter == null) {
            zdt = zdt.plusSeconds(60 - zdt.getSecond());
        }

        int nextLeapYear = CronUtils.getNextLeapYear(timeZone);
        ZonedDateTime maxTime = ZonedDateTime.of(nextLeapYear, 2, 29, 23, 59, 59, 0, timeZone.toZoneId());

        long searchStart = System.currentTimeMillis();
        // search for next matching time
        outer: while (true) {
            // the max next run time using the cron syntax would be the last second of February during the next leap year
            // so if we still didn't find the time, then the syntax is 100% invalid
            if (zdt.isAfter(maxTime)) {
                throw new InvalidCronException("invalid cron syntax");
            }
            for (Parameter parameter : parameters) {
                boolean valid = parameter.isValid(zdt);
                if (!valid) {
//                    System.out.println("%s is invalid for %s parameter".formatted(zdt, parameter.getCronPart().name()));
                    zdt = parameter.nextValid(zdt);
                    continue outer;
                }
            }
            break;
        }
        System.out.println("next run: %s (found in %sms)".formatted(zdt, System.currentTimeMillis() - searchStart));
    }

    public ZonedDateTime getNextRunTime() {
        return zdt;
    }

    public void addWarningBefore(int amount, Duration duration) {

    }
}
