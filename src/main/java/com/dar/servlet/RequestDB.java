package com.dar.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class RequestDB extends HttpServlet {


	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String debug = "ds is null";
		String user_name = null;
	    try
	    {
	        Context ctx = new InitialContext();
	        Context envCtx = (Context) ctx.lookup("java:comp/env");
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParis");
	        if (ds != null)
	        {
	            Connection conn = ds.getConnection();
	            debug = "ds not null";
	            if (conn != null)
	            {
	            	debug = "conn";
	                Statement stmt = conn.createStatement();
	                ResultSet rst = stmt.executeQuery("select firstname from utilisateur where lastname='DOUMOULAKIS';");
	                if (rst.next())
	                {
	                    user_name = rst.getString("firstname");
	                }
	                conn.close();
	            }
	        }
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    PrintWriter out = response.getWriter();
        out.println(debug+" "+user_name);
	}
}
