## A simple cron expression parser & scheduler library for Java.

https://repo.artillex-studios.com/#/releases/com/artillexstudios/axcronscheduler/AxCronScheduler

### Format
`* * * * * *` SECOND (optional), MINUTE, HOUR, DAY, MONTH, DAY OF WEEK

### Examples
`* * * * * *` every second

`* * * * *` every minute

`*/10 * * * *` every 10 minutes

`* * * * MON-TUE,FRI` every minutes on mondays, tuesdays and fridays

`5/10 * * * *` every 10 minutes with a 5 minute offset

`* * * 1-6 *` runs every minute in the first half of the year

`* * * JAN,DEC *` runs every minute during january and december

`* * * * */2` runs every minute every second day (sun, tue, thu, sat)

`* * 1,5,10,15 * *` runs every minute on every month on the defined days

`59 59 23 29 2 *` runs in the last second of leap years' february

### How to use
```java
// create a schedule using a cron expression
Schedule schedule = CronParser.createScheduler("0 * * * * *");

// optional: run a runnable X time before timer is reached
schedule.setEarlyWarning(Duration.of(10, ChronoUnit.SECONDS), () -> {
    System.out.println("this is a warning that is sent 10 seconds before the schedule is executed!");
});

// you will need a ScheduledExecutorService
ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
// start the schedule
schedule.start(executor, () -> {
    // this will run every minute!
    System.out.println("executed!");
});
```
