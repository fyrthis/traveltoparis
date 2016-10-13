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

public class CreateAccount extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("You called this servlet with get method");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder errors = new StringBuilder();
		response.setContentType("text/html");
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
			} else errors.append("incorrect argument.<br>");
			
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
			DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParisDB");
			if (ds != null)
			{
				Connection conn = ds.getConnection();
				if (conn != null)
				{
					Statement stmt = conn.createStatement();
					ResultSet rst = stmt.executeQuery(rq.toString());
					out.println(rq.toString());
					conn.close();
				} else errors.append("Connection to database failed.<br>");
			} else errors.append("Datasource is null.<br>");
		}
		catch (Exception e)
		{
			errors.append("Query ended with an error : <br>");
			errors.append(rq.toString()).append("<br>");
			errors.append(e.getMessage());
		}
		
		out.println("<html>");
	    out.println("<head>");
	    if(errors.length()==0) out.println("<title>Sign up succesful </title>");
	    else out.println("<title>ERROR </title>");
	    out.println("</head>");
	    out.println("<body>");
	    if(errors.length()==0) out.println("<p>Your account has been successfully created !</p>");
	    else out.println(errors.toString());
	    out.println("</body>");
	    out.println("</html>");

		out.close();
	}

}
