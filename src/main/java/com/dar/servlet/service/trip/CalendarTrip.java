package com.dar.servlet.service.trip;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		//TODO : Check identity and permissions
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(buildJSONMock());
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
	
	private JSONObject buildJSONMock() {
		JSONObject obj = new JSONObject();
		
		JSONArray events = new JSONArray();
		
		JSONObject event = new JSONObject();
		event.put("id", "000001");
		event.put("name", "Event name 1");
		event.put("date", "aaaa/mm/dd");
		
		events.add(event);
		
		event = new JSONObject();
		event.put("id", "000002");
		event.put("name", "Event name 2");
		event.put("date", "aaaa/mm/dd");
		
		events.add(event);
		
		obj.put("Events List", events);
		
		return obj;
	}
}
