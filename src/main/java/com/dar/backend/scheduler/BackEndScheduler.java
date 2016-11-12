package com.dar.backend.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dar.backend.scheduler.jobs.WallpaperJob;

@WebListener
public class BackEndScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private static final String webcam = "5568862a7b28535025280c72";

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new WallpaperJob(webcam), 0, 20, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

}