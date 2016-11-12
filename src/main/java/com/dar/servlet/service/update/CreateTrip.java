package com.dar.servlet.service.update;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dar.backend.sql.SQLManager;

public class CreateTrip extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("You called this servlet with get method");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String rq = ("INSERT INTO trips (name, description, begins, ends) VALUES (?,?,?,?) RETURNING id_trip;");
        try {
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
            //stmt.setQueryTimeout(10);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDate(3, date1);
            stmt.setDate(4, date2);
            ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
            int id_trip = (Integer)res.get(0).get("id_trip");

            HttpSession session = request.getSession();

            rq = "INSERT INTO involded (id_user, id_trip, is_admin) SELECT u.id_user, ?, TRUE FROM users u WHERE u.login=?;";
            stmt = conn.prepareStatement(rq);
            stmt.setInt(1,id_trip);
            stmt.setString(2, (String)session.getAttribute("uname"));
            System.out.println("Result : " + mngr.executeUpdate(stmt));
            conn.close();
            response.sendRedirect(request.getContextPath() + "/html/trip.html?id=" + id_trip);
        } catch(Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath());
        }
    }

}
