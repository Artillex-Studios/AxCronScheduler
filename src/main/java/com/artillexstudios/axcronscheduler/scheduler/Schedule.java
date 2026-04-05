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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private ScheduledFuture<?> future = null;
    private Map<Duration, Runnable> warnings = new HashMap<>();
    private Map<ZonedDateTime, Runnable> calculatedWarnings = new HashMap<>();

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
        int loops = 0;
        outer: while (true) {
            if (loops++ >= 1024) {
                throw new InvalidCronException("invalid cron syntax (search limit reached)");
            }
            // the max next run time using the cron syntax would be the last second of February during the next leap year
            // so if we still didn't find the time, then the syntax is 100% invalid
            if (zdt.isAfter(maxTime)) {
                throw new InvalidCronException("invalid cron syntax (max date reached)");
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
//        System.out.println("next run: %s (found in %sms, %s loops)".formatted(zdt, System.currentTimeMillis() - searchStart, loops));
    }

    private void calculateWarnings() {
        calculatedWarnings.clear();
        for (Map.Entry<Duration, Runnable> entry : warnings.entrySet()) {
            ZonedDateTime warnTime = zdt.minus(entry.getKey());
            if (System.currentTimeMillis() >= warnTime.toInstant().toEpochMilli()) {
                continue;
            }
            calculatedWarnings.put(warnTime, entry.getValue());
        }
    }

    public void setEarlyWarning(Duration duration, Runnable runnable) {
        warnings.put(duration, runnable);
        calculateWarnings();
    }

    public void removeEarlyWarning(Duration duration) {
        warnings.remove(duration);
        calculateWarnings();
    }

    public void clearWarnings() {
        warnings.clear();
        calculatedWarnings.clear();
    }

    public void start(ScheduledExecutorService executor, Runnable runnable) {
        stop();
        future = executor.scheduleAtFixedRate(() -> {
            for (Iterator<Map.Entry<ZonedDateTime, Runnable>> it = calculatedWarnings.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<ZonedDateTime, Runnable> entry = it.next();
                if (System.currentTimeMillis() >= entry.getKey().toInstant().toEpochMilli()) {
                    entry.getValue().run();
                    it.remove();
                }
            }
            if (System.currentTimeMillis() >= zdt.toInstant().toEpochMilli()) {
                calculateNextRunTime();
                calculateWarnings();
                runnable.run();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (future == null) return;
        future.cancel(true);
    }

    public ZonedDateTime getNextRunTime() {
        return zdt;
    }

    public long getNextRunTimeMillis() {
        return zdt.toInstant().toEpochMilli();
    }
}
