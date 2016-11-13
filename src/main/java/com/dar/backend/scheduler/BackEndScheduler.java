package com.dar.backend.scheduler;

import java.sql.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dar.backend.scheduler.jobs.EventJob;
import com.dar.backend.scheduler.jobs.WallpaperJob;

@WebListener
public class BackEndScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler1;
    private ScheduledExecutorService scheduler2;
    private static final String webcam = "5568862a7b28535025280c72";

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        scheduler1 = Executors.newSingleThreadScheduledExecutor();
        scheduler2 = Executors.newSingleThreadScheduledExecutor();
        scheduler1.scheduleAtFixedRate(new EventJob(new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime() + (1000 * 60 * 60 /* *24 */))), 0, 24, TimeUnit.HOURS);
        scheduler2.scheduleAtFixedRate(new WallpaperJob(webcam), 5, 20, TimeUnit.MINUTES);
        //TODO: check so that it doesn't make the database go boom!
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler1.shutdownNow();
        scheduler2.shutdownNow();
    }
}