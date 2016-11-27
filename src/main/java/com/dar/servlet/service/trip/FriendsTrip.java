package com.dar.servlet.service.trip;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dar.backend.sql.Trip;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class FriendsTrip
 */
public class FriendsTrip extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendsTrip() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String id = request.getParameter("trip_id");
        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        try {
            Trip trip = new Trip(new Integer(id));
            obj.put("users", trip.getTripParticipants());
        } catch (Exception e){
            obj.put("status", "fail");
            out.print(obj);
            out.close();
            return;
        }
        obj.put("status", "success");
        out.print(obj);
        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject object = new JSONObject();
        String trip_id = request.getParameter("trip_id");
        String[] user = request.getParameterValues("users[]");
        try {
            Trip trip = new Trip(new Integer(trip_id));
            trip.addUserToTrip(user);
        } catch (Exception e){
            object.put("status", "fail");
            out.print(object);
            out.close();
            return;
        }
        out.print(object);
        out.flush();
        out.close();
    }

}
