package com.artillexstudios.axcronscheduler;

import com.artillexstudios.axcronscheduler.parser.CronParser;
import com.artillexstudios.axcronscheduler.scheduler.Schedule;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// this class is used for testing, it is not part of the library
public class AxCronScheduler {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
//        CronParser.createScheduler("* * * 31 feb-dec *");
//
//        CronParser.createScheduler("59 59 23 29 2 *");
//
//        CronParser.createScheduler("* * * * * *");
//
//        Schedule schedule = CronParser.createScheduler("* * * * *");
//        schedule.setEarlyWarning(Duration.of(10, ChronoUnit.SECONDS), () -> {
//            System.out.println("10s early warning " + ZonedDateTime.now());
//        });
//        schedule.setEarlyWarning(Duration.of(20, ChronoUnit.SECONDS), () -> {
//            System.out.println("20s early warning " + ZonedDateTime.now());
//        });
//        schedule.setEarlyWarning(Duration.of(30, ChronoUnit.SECONDS), () -> {
//            System.out.println("30s early warning " + ZonedDateTime.now());
//        });
//        schedule.setEarlyWarning(Duration.of(40, ChronoUnit.SECONDS), () -> {
//            System.out.println("40s early warning " + ZonedDateTime.now());
//        });
//        schedule.setEarlyWarning(Duration.of(50, ChronoUnit.SECONDS), () -> {
//            System.out.println("50s early warning " + ZonedDateTime.now());
//        });
//        schedule.start(executor, () -> {
//            System.out.println("execute! " + ZonedDateTime.now());
//        });
//
//        CronParser.createScheduler("* * * DEC-FEB *");
//
//        CronParser.createScheduler("5/30 * * * * 1/2");
//
//        CronParser.createScheduler("* * * * * */2");
//
//        CronParser.createScheduler("0 14-22/2 * * *");
    }
}
