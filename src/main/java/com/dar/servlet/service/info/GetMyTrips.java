package com.dar.servlet.service.info;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dar.Tools;
import com.dar.backend.sql.User;
import org.json.simple.JSONObject;

public class GetMyTrips extends HttpServlet {
    //Returned a JSON of user's trips

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        JSONObject obj = new JSONObject();
        if(session.isNew()){
            obj.put("status", "fail");
            obj.put("cause", "not connected");
            out.print(obj);
            out.close();
            return;
        }
        String uname = (String)session.getAttribute("uname");
        User user;
        try {
            user = new User(uname);
            obj.put("trips", user.getUserTrips());
        } catch (Exception e){e.printStackTrace(out); out.close();}
        obj.put("status","success");
        out.print(obj);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
