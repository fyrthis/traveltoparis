package com.dar.servlet.service.trip;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dar.backend.sql.SQLManager;
import com.dar.backend.sql.Trip;

/**
 * Servlet implementation class SettingsTrip
 */
@WebServlet("/SettingsTrip")
public class SettingsTrip extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SettingsTrip() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Check identity and permissions
		HttpSession session = request.getSession(false);
		String uname = (String) session.getAttribute("uname");
		int id_trip = Integer.parseInt(request.getParameter("id"));
		try{
			if(!Trip.checkIfUserIsInTrip(id_trip, uname)){
				System.out.println(new java.util.Date().toString() + " | Illegal trip access attemps " + " Uname : " + uname + " Trip : " + id_trip);
				response.sendRedirect(request.getContextPath());
			}
		}catch (Exception e){
			e.printStackTrace();
			response.sendRedirect(request.getContextPath());
		}
		//Renvoyer les informations relatives au trip
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			Trip trip = new Trip(id_trip);
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
		}
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("SettingsTrip POST");
			//Check dentity
			//Check identity and permissions
			HttpSession session = request.getSession(false);
			String uname = (String) session.getAttribute("uname");
			int id_trip = Integer.parseInt(request.getParameter("id"));
			if(!Trip.checkIfUserIsInTrip(id_trip, uname)){
				System.out.println(new java.util.Date().toString() + " | Illegal trip access attemps " + " Uname : " + uname + " Trip : " + id_trip);
				response.sendRedirect(request.getContextPath());
			}
			//Update le trip
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String begins = request.getParameter("begins");
			String ends = request.getParameter("ends");
			System.out.println("name " + name);
			System.out.println("des " + description);
			System.out.println("begin " + begins);
			System.out.println("end " + ends);
			Date date1;
			Date date2;
			Calendar cal = Calendar.getInstance();
			PreparedStatement stmt = null;
			String rq = ("UPDATE trips SET name=?, description=?, begins=?, ends=? WHERE id_trip=?;");
			System.out.println(rq);

			String[] list = begins.split("-");
			cal.set(Calendar.YEAR, Integer.parseInt(list[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(list[1]));
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(list[2]));
			date1 = new Date(cal.getTimeInMillis());
			list = ends.split("-");
			cal.set(Calendar.YEAR, Integer.parseInt(list[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(list[1]));
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(list[2]));
			date2 = new Date(cal.getTimeInMillis());

			SQLManager mngr = new SQLManager();
			Connection conn = mngr.getConnection();
			stmt = conn.prepareStatement(rq);
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setDate(3, date1);
			stmt.setDate(4, date2);
			stmt.setInt(5, id_trip);

			System.out.println("Result : " + mngr.executeUpdate(stmt));
			conn.close();
			response.sendRedirect(request.getContextPath() + "/html/trip.html?id=" + id_trip);
		} catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath());
		}
	}
}
