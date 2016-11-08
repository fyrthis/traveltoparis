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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetMyTrips extends HttpServlet {
    //Returned a JSON of user's trips

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        if(session.isNew()){
            JSONObject obj = new JSONObject();
            obj.put("Status", "fail");
            obj.put("Cause", "not connected");
            out.print(obj);
            Tools.closeConn(out);
            return;
        }
        String uname = (String)session.getAttribute("uname");
        User user = null;
        try {
          user = new User(uname);
        } catch (Exception e){e.printStackTrace(out); Tools.closeConn(out);}



        out.print(buildJSON());
        Tools.closeConn(out);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

    private JSONObject buildJSON() {
        JSONObject obj = new JSONObject();
        obj.put("Name", "crunchify.com");
        obj.put("Author", "App Shah");

        JSONArray company = new JSONArray();
        company.add("Compnay: eBay");
        company.add("Compnay: Paypal");
        company.add("Compnay: Google");
        obj.put("Company List", company);

        return obj;
    }
}
