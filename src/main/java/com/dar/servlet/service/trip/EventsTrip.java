package com.dar.servlet.service.trip;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dar.Tools;
import com.dar.backend.scheduler.jobs.EventJob;
import com.dar.backend.sql.Trip;
import com.dar.backend.sql.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class EventsTrip
 */
@WebServlet("/EventsTrip")
public class EventsTrip extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventsTrip() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        //System.out.println("DEBUG " + request.getRequestURL().append('?').append(request.getQueryString()));
        try {
            String trip_id = request.getParameter("id");
            String start_dateS = request.getParameter("begins");
            Date start = Tools.dateOfString(start_dateS);
            String end_dateS = request.getParameter("ends");
            Date end = Tools.dateOfString(end_dateS);
            String[] cat = request.getParameterValues("categories[]");
            String sortBy = request.getParameter("sortby");
            int page = Integer.parseInt(request.getParameter("page"));
            if(page<0) throw new Exception("Someone tried to shoot us down with a negative page number !");
            Trip trip = new Trip(Integer.parseInt(trip_id));
            start = trip.getBegins();
            end = trip.getEnds();
            long diff = end.getTime() - new java.util.Date().getTime();
            int no_of_days = Math.round(diff / Tools.MILLISECONDS_IN_DAY);
            //System.out.println("DEBUG ID " + trip_id + " | S " + start.toString() + " | E " + end.toString() + " | SB " + sortBy);
            //if(cat != null)System.out.println("DEBUG CAT " + cat);
            //System.out.println("DEBUG NO DAYS " + no_of_days);
            if(no_of_days > 1){
                System.out.println(new java.util.Date().toString() + " | Getting future events on demand");
                ExecutorService executor = (ExecutorService)getServletContext().getAttribute("MY_EXECUTOR");
                executor.submit(new EventJob(start, end));
                if(!executor.awaitTermination(2, TimeUnit.MINUTES)){
                    throw new Exception("Waiting too long");
                }
            }
            /*TODO : epic super complicated machine learning neural network quantic algorithm of space time bending awsomeness
             that gives good event suggestions with EventJob runnable, or get one (thousand?) Indian guy(s?) to do it.*/
            obj.put("events", trip.chooseNewEvents(cat, start, end, sortBy, page));
        } catch (Exception e){
            e.printStackTrace(System.out);
            System.out.flush();
            obj.put("status", "fail");
            out.print(obj);
            out.close();
            return;
        }
        obj.put("status", "success");
        out.print(obj);
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // rajoute un upvote pour un event ce qui a pour effet de bord de l'ajouter a un trip
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject object = new JSONObject();
        String event_id = request.getParameter("event_id");
        String trip_id = request.getParameter("trip_id");
        String uname = (String) session.getAttribute("uname");
        try {
            User user = new User(uname);
            user.addEventToTrip(Integer.parseInt(trip_id), event_id, true);
        } catch (Exception e){
            e.printStackTrace();
            object.put("status", "failed");
            out.print(object);
            out.close();
            return;
        }
        object.put("status", "success");
        out.print(object);
        out.close();
    }
}
