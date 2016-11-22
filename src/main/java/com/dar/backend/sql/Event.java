package com.dar.backend.sql;


import org.json.simple.JSONObject;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Event implements JSONable{
    private int id;
    private String name;
    private String url;
    private String location;
    private Date date;
    private String description;


    public Event(int id) throws NamingException, SQLException {
        String request = "SELECT * FROM events WHERE id_event=?;";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        conn.close();
        HashMap<String, Object> first = res.get(0);
        this.id = (Integer)first.get("id_event");
        this.name = (String)first.get("name");
        this.url = (String)first.get("url");
        this.location = (String)first.get("location");
        this.date = (Date)first.get("date");
        this.description = (String)first.get("description");
    }

    public Event(int id, String name, String url, String location, Date date, String description){
        this.id = id;
        this.name = name;
        this.url = url;
        this.location = location;
        this.date = date;
        this.description = description;
    }

    public static void insertEvent(String id, String name, String url, String location, Date date, String description) throws NamingException, SQLException {
        System.out.println("EXISTS id : " + id);
        System.out.flush();
        String request = "SELECT exists(SELECT 1 FROM events WHERE id_event=?)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        System.out.println("Executed");
        System.out.flush();
        Boolean exists = (Boolean) res.get(0).get("exists");
        if (!exists) {
            request = "INSERT INTO events (id_event, name, url, location, eventdate, description) VALUES (?,?,?,?,?,?)";
            stmt = conn.prepareStatement(request);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, url);
            stmt.setString(4, location);
            stmt.setDate(5, date);
            stmt.setString(6, description);
            mngr.executeUpdate(stmt);
            conn.close();
        }
    }

    public static void insertTag(String id_event, String id_category) throws NamingException, SQLException {
        String request = "SELECT exists(SELECT 1 FROM tagged WHERE id_event=? AND id_category=?)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, id_event);
        stmt.setString(2, id_category);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        Boolean exists = (Boolean) res.get(0).get("exists");
        if (!exists) {
            request = "INSERT INTO tagged (id_event, id_category) SELECT ?, t.id_category FROM categories t WHERE t.name=?";
            mngr = new SQLManager();
            conn = mngr.getConnection();
            stmt = conn.prepareStatement(request);
            stmt.setString(1, id_event);
            stmt.setString(2, id_category);
            mngr.executeUpdate(stmt);
            conn.close();
        }
    }

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", "event");
        obj.put("name", name);
        obj.put("url", url);
        obj.put("location", location);
        obj.put("date", date.toString());
        obj.put("description", description);
        return obj;
    }
}
