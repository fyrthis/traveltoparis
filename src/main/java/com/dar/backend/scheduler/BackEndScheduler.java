package com.dar.backend.scheduler;

import java.sql.Date;
import java.util.concurrent.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dar.backend.scheduler.jobs.EventJob;
import com.dar.backend.scheduler.jobs.WallpaperJob;

@WebListener
public class BackEndScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private ExecutorService executor;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        int nr_executors = 1;
        ThreadFactory daemonFactory = new DaemonThreadFactory();

        try {
            nr_executors = Integer.parseInt(context.getInitParameter("nr-executors"));
        } catch (NumberFormatException ignore ) {}

        if(nr_executors <= 1) {
            executor = Executors.newSingleThreadExecutor(daemonFactory);
        } else {
            executor = Executors.newFixedThreadPool(nr_executors,daemonFactory);
        }
        context.setAttribute("MY_EXECUTOR", executor);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new EventJob(new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime() + (1000 * 60 * 60 * 24 * 7))), 0, 30, TimeUnit.HOURS);
        scheduler.scheduleAtFixedRate(new WallpaperJob(), 5, 20, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println(new java.util.Date().toString() + " | Context destroyed");
        //ServletContext context = event.getServletContext();
        executor.shutdownNow();
        scheduler.shutdownNow();
    }
}