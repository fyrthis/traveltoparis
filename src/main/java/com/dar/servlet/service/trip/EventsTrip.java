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
        String trip_id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            Trip trip = new Trip(new Integer(trip_id));
            JSONObject object = trip.getTripEvents();
            out.print(object);
        } catch (Exception e){
            e.printStackTrace();
            response.sendRedirect(request.getContextPath());
        }
        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private JSONObject buildJSON() {
        return null;
    }

}
