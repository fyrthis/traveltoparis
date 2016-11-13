package com.dar.backend.scheduler.jobs;


import com.dar.backend.api.Eventful;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.Category;

public class EventJob implements Runnable{
    private final Date begin;
    private final Date end;
    private final ArrayList<Category> filtre;


    public EventJob(Date b, Date e){
        this.begin = b;
        this.end = e;
        this.filtre = null;
    }

    public EventJob(Date b, Date e, ArrayList<Category> cat){
        this.begin = b;
        this.end = e;
        this.filtre = cat;
    }

    @Override
    public void run() {
        String begin = this.begin.toString().replaceAll("-","");
        String end = this.end.toString().replaceAll("-", "");
        System.out.println(new java.util.Date().toString() + " | Launching background routine Events : " + begin + " | " + end);
        List<Event> list = new ArrayList<>();
        try {
            list = Eventful.getEvents(begin, end);
        } catch (Exception e){e.printStackTrace();}
        //if(filtre == null)list = Eventful.getEvents(begin, end);
        //else list = Eventful.getEvents(begin, end, filtre);
        System.out.println(new java.util.Date().toString() + " | Got Events " + list.size());
        for(Event e : list){
            String name = e.getTitle();
            String desc = e.getDescription();
            String location = e.getVenueAddress();
            Date date = new Date(e.getStartTime().getTime());
            String url = e.getURL();
            try {
                int event_id = com.dar.backend.sql.Event.insertEvent(name, url, location, date, desc);
                List<Category> cat_list = e.getCategories();
                for(Category cat : cat_list){
                    com.dar.backend.sql.Event.insertTag(event_id, cat.getId());
                }
            } catch (Exception ex){ex.printStackTrace();}
        }
    }
}
