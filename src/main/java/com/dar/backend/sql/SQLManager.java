package com.dar.backend.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SQLManager {
	
	public SQLManager() { }

	private Connection getConnection() throws NamingException, SQLException {
		Context ctx = new InitialContext();
		Context envCtx = (Context) ctx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParisDB");
		if (ds != null)
		{
			Connection conn = ds.getConnection();
			if (conn != null) {
				return conn;

			} else throw new SQLException("Connection to database failed.<br>");
		} else throw new SQLException("Datasource is null.<br>");
	}
	
	ResultSet executeQuery(String request) throws NamingException, SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
	        ResultSet rst = stmt.executeQuery(request);
	        conn.close();
	        return rst;
		} finally {
			try {
				if(conn != null) {
					if(!conn.isClosed()) {
						conn.close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int executeUpdate(String request) throws SQLException, NamingException {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			int rst = stmt.executeUpdate(request);
			conn.close();
	        return rst;
		} finally {
			try {
				if(conn != null) {
					if(!conn.isClosed()) {
						conn.close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

