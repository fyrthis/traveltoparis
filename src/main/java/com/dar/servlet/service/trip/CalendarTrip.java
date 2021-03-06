package com.dar.servlet.service.trip;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dar.backend.sql.Trip;
import com.dar.backend.sql.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class CalendarTrip
 */
@WebServlet("/CalendarTrip")
public class CalendarTrip extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalendarTrip() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        JSONObject obj = new JSONObject();
        String trip_id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            int user_id = new User((String)session.getAttribute("uname")).getId();
            Trip trip = new Trip(Integer.parseInt(trip_id));
            JSONObject object = trip.getTripEvents(user_id);
            obj.put("events", object);
        } catch (Exception e){
            e.printStackTrace();
            obj.put("status", "failure");
            out.print(obj);
            return;
            //response.sendRedirect(request.getContextPath());
        }
        obj.put("status", "success");
        out.print(obj);
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject object = new JSONObject();
        String event_id = request.getParameter("event_id");
        String like = request.getParameter("vote");
        boolean remove = Boolean.parseBoolean(request.getParameter("remove"));
        String trip_id = request.getParameter("trip_id");
        String uname = (String) session.getAttribute("uname");
        try {
            User user = new User(uname);
            if(!remove) user.addEventToTrip(Integer.parseInt(trip_id), event_id, Boolean.parseBoolean(like));
            else user.removeVote(Integer.parseInt(trip_id), event_id);
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

    /**
     * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        JSONObject object = new JSONObject();
        PrintWriter out = response.getWriter();
        String event_id = request.getHeader("event_id");
        String trip_id = request.getHeader("trip_id");
        String uname = (String) session.getAttribute("uname");
        try {
            User user = new User(uname);
            user.removeEventFromTrip(Integer.parseInt(trip_id), event_id);
        } catch (Exception e){
            e.printStackTrace();
            object.put("status", "failure");
            out.print(object);
            return;
        }
        object.put("status", "success");
        out.print(object);
        out.close();
    }


}
