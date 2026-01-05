package com.artillexstudios.axcronscheduler;

import com.artillexstudios.axcronscheduler.parser.CronParser;

public class AxCronScheduler {

    public static void main(String[] args) {
        CronParser.createScheduler("* * * 31 feb-dec *");
        // next run: 2026-03-31T00:00+02:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("59 59 23 29 2 *");
        // next run: 2028-02-29T23:59:59+01:00[Europe/Budapest] (found in 2ms)

        CronParser.createScheduler("* * * * *");
        // next run: 2026-01-05T13:52+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("* * * * * *");
        // next run: 2026-01-05T13:51:10+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("* * * DEC-FEB *");
        // next run: 2026-01-05T13:52+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("5/30 * * * * 1/2");
        // next run: 2026-01-05T13:51:35+01:00[Europe/Budapest] (found in 0ms)

        CronParser.createScheduler("* * * * * */2");
        // next run: 2026-01-06T00:00+01:00[Europe/Budapest] (found in 0ms)
    }
}
