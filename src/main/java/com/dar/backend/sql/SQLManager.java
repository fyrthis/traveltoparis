package com.dar.backend.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SQLManager {

    public SQLManager() { }

    public Connection getConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        Context envCtx = (Context) ctx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParisDB");
        if (ds != null) {
            Connection conn = ds.getConnection();
            if (conn != null) {
                return conn;

            } else throw new SQLException("Connection to database failed.<br>");
        } else throw new SQLException("Datasource is null.<br>");
    }

    public ArrayList<HashMap<String, Object>> executeQuery(PreparedStatement prep) throws NamingException, SQLException {
        ArrayList<HashMap<String, Object>> res = new ArrayList<>();
        try {
            ResultSet rs = prep.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while (rs.next()) {
                HashMap<String,Object> row = new HashMap<>(columns);
                for(int i=1; i<=columns; ++i) {
                    row.put(metaData.getColumnName(i),rs.getObject(i));
                }
                res.add(row);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int executeUpdate(PreparedStatement prep) throws SQLException, NamingException {
        try {
            return prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

