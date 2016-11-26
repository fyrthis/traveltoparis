package com.dar.servlet.service.session;

import com.dar.Tools;
import com.dar.backend.sql.SQLManager;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


public class SignIn extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        JSONObject obj = new JSONObject();
        if(session == null) obj.put("has_session", false);
        else obj.put("has_session", true);
        //response.sendRedirect(request.getContextPath());
        out.print(obj);
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String requestDb = "SELECT u.password, u.salt FROM Users u WHERE u.login = ?";
        String uname = request.getParameter("uname");
        String password = request.getParameter("psw");
        String dbHashedPass = null;
        String dbSalt = null;
        SQLManager sql = new SQLManager();
        JSONObject result = new JSONObject();
        try {
            Connection con = sql.getConnection();
            PreparedStatement stmt = con.prepareStatement(requestDb);
            stmt.setString(1, uname);
            ArrayList<HashMap<String, Object>> res = sql.executeQuery(stmt);
            if(res.size() >= 1){
                dbHashedPass = (String)res.get(0).get("password");
                dbSalt = (String)res.get(0).get("salt");
            }
            con.close();
        } catch (Exception e){
            e.printStackTrace();
            result.put("requestValid", "no");
            out.print(result);
            out.close();
            return;
        }
        // format: algorithm:iterations:hashSize:salt:hash
        String param = "sha256:" + Tools.PBKDF2_ITERATIONS + ":" + Tools.HASH_BYTE_SIZE + ":" + dbSalt + ":" + dbHashedPass;
        boolean isValid;
        try {
            isValid = Tools.verifyPassword(password, param);
        } catch (Exception e){
            e.printStackTrace();
            result.put("requestValid", "no");
            out.print(result);
            out.close();
            return;
        }
        //System.out.println("DEBUG : cheking done");
        if(isValid){
            result.put("requestValid", "yes");
            HttpSession session = request.getSession(true);
            if(session.isNew()){
                result.put("sessionType", "new");
                session.setAttribute("uname", uname);
                session.setAttribute("hash", dbHashedPass);
            }
            else{result.put("sessionType", "old");}
        } else{result.put("requestValid", "no");}
        out.print(result);
        out.close();
    }
}
