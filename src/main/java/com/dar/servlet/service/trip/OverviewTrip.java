package com.dar.servlet.service.trip;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import com.dar.backend.sql.Trip;

/**
 * Servlet implementation class ResumeTrip
 */
@WebServlet("/OverviewTrip")
public class OverviewTrip extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OverviewTrip() {
        super();
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String trip_id = request.getParameter("id");
        try {
            Trip trip = new Trip(Integer.parseInt(trip_id));
            if(trip.getId() == -1){
                JSONObject obj = new JSONObject();
                obj.put("status", "failure");
                out.print(obj);
                out.close();
                response.sendRedirect(request.getContextPath());
                return;
            }
            JSONObject object = trip.getTripOverview();
            object.put("status", "success");
            out.print(object);
        } catch (Exception e){
            e.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("status", "failure");
            out.print(obj);
            out.close();
            //response.sendRedirect(request.getContextPath());
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
}
