package com.dar.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class SignUp extends HttpServlet {

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		//BUILD QUERY FROM POST PARAMETERS
		Enumeration<String> parameterNames = request.getParameterNames();
		StringBuilder rq = new StringBuilder("INSERT INTO Users (");
		StringBuilder rq2 = new StringBuilder(") VALUES (");
		while (parameterNames.hasMoreElements()) {
			String param = parameterNames.nextElement();
			rq.append(param);
			String[] paramValues = request.getParameterValues(param);
			if(paramValues.length==1) {
				rq2.append("'").append(paramValues[0]).append("'");
			} else throw new IOException("unexpected value");
			
			if(parameterNames.hasMoreElements()){
				rq.append(", ");
				rq2.append(", ");
			}
		}
		
		rq.append(rq2).append(");");
		
		//EXECUTE DATABASE QUERY
		try
		{
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParis");
			if (ds != null)
			{
				Connection conn = ds.getConnection();
				if (conn != null)
				{
					Statement stmt = conn.createStatement();
					ResultSet rst = stmt.executeQuery(rq.toString());
					out.println(rq.toString());
					conn.close();
				}
			}
		}
		catch (Exception e)
		{
			out.println("exception !");
			e.printStackTrace();
		}
		out.close();
	}

}
