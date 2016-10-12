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
		PrintWriter out = response.getWriter();
		out.println("You called this servlet with get method");
		out.close();
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
			} else out.println("unexpected value<br>");
			
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
				} else out.println("connection refused<br>");
			}
		}
		catch (Exception e)
		{
			out.println("failed to execute databse query<br>");
			e.printStackTrace();
		}
		out.close();
	}

}
