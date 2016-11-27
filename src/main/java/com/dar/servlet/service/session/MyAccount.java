package com.dar.servlet.service.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.dar.Tools;
import com.dar.Validator;
import com.dar.backend.sql.SQLManager;
import com.dar.backend.sql.User;


public class MyAccount extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try{
			//Check identity
			HttpSession session = request.getSession(false);
			if(session == null) throw new Exception("Someone tried to access a user zone, without any session.");
			String uname = (String) session.getAttribute("uname");

			//Renvoyer les informations relatives au user
			response.setContentType("application/json");
			User user = new User(uname);
			out.print(user.getJSON());
			System.out.println(new java.util.Date().toString() + "MyAccount GET : OK");
		} catch (Exception e){
			e.printStackTrace();
			out.close();
			response.sendRedirect(request.getContextPath());
			return;
		}
		out.flush();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//NOte : we consider username cannot be change.
		// First : Check identity and getParameters
		response.setContentType("application/json");
		Connection conn = null;
		try{
			System.out.println(new java.util.Date().toString() + "MyAccount POST : START");
			//Check identity
			HttpSession session = request.getSession(false);
			if(session == null) throw new Exception("Someone tried to access a user zone, without any session.");
			
			String uname = (String) session.getAttribute("uname");
			String cur_password = request.getParameter("curpassword");
			if(!checkPassword(uname, cur_password)) throw new Exception("Wrong password while attempting to change user's data.");
			System.out.println(new java.util.Date().toString() + "MyAccount POST : ID IS OK");
			//getParameters
	        String firstname = request.getParameter("firstname");
	        String lastname = request.getParameter("lastname");
	        String email = request.getParameter("email");
	        String birth = request.getParameter("birthday");
	        String country = request.getParameter("country");
	        
	        String password = request.getParameter("password");
	        String securePass, salt;
	        //Check parameters
	        Validator v = new Validator();
	        if(!v.firstname(firstname)) throw new Exception("firstname : bad pattern.");
	        if(!v.lastname(lastname)) throw new Exception("lastname : bad pattern.");
	        if(!v.email(email)) throw new Exception("email : bad pattern.");
	        System.out.println(new java.util.Date().toString() + "MyAccount POST : PARAMS ARE OK");
		//Second : If password field is not empty, change it
	        String rq;
	        Calendar cal = Calendar.getInstance();
	        PreparedStatement stmt = null;
	        SQLManager mngr = new SQLManager();
			conn = mngr.getConnection();
	        if(password.length() > 4) {
	        	String[] list = birth.split("-");
	            cal.set(Calendar.YEAR, Integer.parseInt(list[0]));
	            cal.set(Calendar.MONTH, Integer.parseInt(list[1]));
	            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(list[2]));
	            String hashedReturn = Tools.createHash(password);
	            list = hashedReturn.split(":");
	            salt = list[3];
	            securePass = list[4];
	            rq = ("UPDATE users SET password=?, salt=?, firstname=?, lastname=?, birthday=?, country=?, email=? WHERE login=?;");
	          //Third : Put data into statement
	            stmt = conn.prepareStatement(rq);
	            stmt.setString(1, securePass);
	            stmt.setString(2, salt);
	            stmt.setString(3, firstname);
	            stmt.setString(4, lastname);
	            stmt.setDate(5, new Date(cal.getTimeInMillis()));
	            stmt.setString(6, country);
	            stmt.setString(7, email);
	            stmt.setString(8, uname);
	        } else {
	        	rq = ("UPDATE users SET firstname=?, lastname=?, birthday=?, country=?, email=? WHERE login=?;");
	        	//Third : Put data into statement
	        	stmt = conn.prepareStatement(rq);
	            stmt.setString(1, firstname);
	            stmt.setString(2, lastname);
	            stmt.setDate(3, new Date(cal.getTimeInMillis()));
	            stmt.setString(4, country);
	            stmt.setString(5, email);
	            stmt.setString(6, uname);
	        }
	        System.out.println(new java.util.Date().toString() + "MyAccount POST : STMT IS READY");
            stmt.executeUpdate();
            System.out.println(new java.util.Date().toString() + "MyAccount POST : STMT SUCEEDED");
            conn.close();
		} catch (Exception e){
        	if(conn!= null)
				try {
					if(!conn.isClosed()) conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
        	e.printStackTrace();
        }
	}
		
		private boolean checkPassword(String uname, String password) {
			String requestDb = "SELECT u.password, u.salt FROM Users u WHERE u.login = ?";
	        String dbHashedPass = null;
	        String dbSalt = null;
	        SQLManager sql = new SQLManager();
	        Connection conn = null;
	        try {
	        	conn = sql.getConnection();
	            PreparedStatement stmt = conn.prepareStatement(requestDb);
	            stmt.setString(1, uname);
	            ArrayList<HashMap<String, Object>> res = sql.executeQuery(stmt);
	            if(res.size() >= 1){
	                dbHashedPass = (String)res.get(0).get("password");
	                dbSalt = (String)res.get(0).get("salt");
	            }
	            conn.close();
	        } catch (Exception e){
	        	if(conn!= null)
					try {
						if(!conn.isClosed()) conn.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
	        	return false;
	        }

	        String param = "sha256:" + Tools.PBKDF2_ITERATIONS + ":" + Tools.HASH_BYTE_SIZE + ":" + dbSalt + ":" + dbHashedPass;
	        boolean isValid;
	        try {
	            isValid = Tools.verifyPassword(password, param);
	        } catch (Exception e){
	            return false;
	        }

	        return isValid;
		}
}
