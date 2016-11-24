package com.dar.backend.scheduler.jobs;


import com.dar.backend.api.Eventful;
import java.sql.Date;

public class EventJob implements Runnable{
    private final Date begin;
    private final Date end;

    public EventJob(Date b, Date e){
        this.begin = b;
        this.end = e;
    }

    @Override
    public void run() {
        try {
            String begin = this.begin.toString().replaceAll("-","");
            String end = this.end.toString().replaceAll("-", "");
            System.out.println(new java.util.Date().toString() + " | Launching background routine Events : " + begin + " | " + end);
            Eventful ev = new Eventful();
            ev.updateDatabase(begin, end);
            ev.removePastEvents();
        } catch (Exception ex){ex.printStackTrace();}
        System.out.println(new java.util.Date().toString() + " | Done");
    }
}
