package com.artillexstudios.axcronscheduler;

import com.artillexstudios.axcronscheduler.parser.CronParser;
import com.artillexstudios.axcronscheduler.scheduler.Schedule;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AxCronScheduler {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        CronParser.createScheduler("* * * 31 feb-dec *");
        // next run: 2026-03-31T00:00+02:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("59 59 23 29 2 *");
        // next run: 2028-02-29T23:59:59+01:00[Europe/Budapest] (found in 2ms)

        CronParser.createScheduler("* * * * * *");
        // next run: 2026-01-05T13:51:10+01:00[Europe/Budapest] (found in 0ms)

        Schedule schedule = CronParser.createScheduler("* * * * *");
        schedule.setEarlyWarning(Duration.of(10, ChronoUnit.SECONDS), () -> {
            System.out.println("10s early warning " + ZonedDateTime.now());
        });
        schedule.setEarlyWarning(Duration.of(20, ChronoUnit.SECONDS), () -> {
            System.out.println("20s early warning " + ZonedDateTime.now());
        });
        schedule.setEarlyWarning(Duration.of(30, ChronoUnit.SECONDS), () -> {
            System.out.println("30s early warning " + ZonedDateTime.now());
        });
        schedule.setEarlyWarning(Duration.of(40, ChronoUnit.SECONDS), () -> {
            System.out.println("40s early warning " + ZonedDateTime.now());
        });
        schedule.setEarlyWarning(Duration.of(50, ChronoUnit.SECONDS), () -> {
            System.out.println("50s early warning " + ZonedDateTime.now());
        });
        schedule.start(executor, () -> {
            System.out.println("execute! " + ZonedDateTime.now());
        });
        // next run: 2026-01-05T13:52+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("* * * DEC-FEB *");
        // next run: 2026-01-05T13:52+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("5/30 * * * * 1/2");
        // next run: 2026-01-05T13:51:35+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("* * * * * */2");
        // next run: 2026-01-06T00:00+01:00[Europe/Budapest] (found in 0ms)
    }
}
