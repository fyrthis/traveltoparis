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
    private Date begin;
    private Date end;
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
        this.begin = (Date)first.get("eventbegin");
        this.end = (Date)first.get("eventend");
        this.description = (String)first.get("description");
    }

    public Event(int id, String name, String url, String location, Date begin, Date end, String description){
        this.id = id;
        this.name = name;
        this.url = url;
        this.location = location;
        this.begin = begin;
        this.end = end;
        this.description = description;
    }

    public static void insertEvent(String id, String name, String url, String location, Date begin, Date end, String description) throws NamingException, SQLException {
        //String request = "SELECT exists(SELECT 1 FROM events WHERE id_event=?)";
        String request = "INSERT INTO events (id_event, name, url, location, eventbegin, eventend, description) SELECT ?,?,?,?,?,?,? " +
                "WHERE NOT exists(SELECT 1 FROM events WHERE id_event=?)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, id);
        stmt.setString(2, name);
        stmt.setString(3, url);
        stmt.setString(4, location);
        stmt.setDate(5, begin);
        stmt.setDate(6, end);
        stmt.setString(7, description);
        stmt.setString(8, id);
        mngr.executeUpdate(stmt);
        conn.close();
    }

    public static void insertTag(String id_event, String name_category) throws NamingException, SQLException {
        String request = "INSERT INTO tagged (id_event, id_category) SELECT ?, c.id_category FROM categories c " +
                "WHERE c.name=? AND NOT exists(SELECT 1 FROM tagged t, categories c1 WHERE t.id_event=? AND c1.name=? AND t.id_category=c1.id_category)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, id_event);
        stmt.setString(2, name_category);
        stmt.setString(3, id_event);
        stmt.setString(4, name_category);
        //System.out.println(stmt.toString());
        //System.out.flush();
        mngr.executeUpdate(stmt);
        conn.close();
    }

    public static void removePast(Date date) throws NamingException, SQLException{
        String request = "DELETE FROM events e WHERE e.eventend < ? AND NOT exists(SELECT 1 FROM votes v WHERE v.id_event = e.id_event)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setDate(1, date);
        conn.close();
    }

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", "event");
        obj.put("id", id);
        obj.put("name", name);
        obj.put("url", url);
        obj.put("location", location);
        obj.put("begin", begin.toString());
        obj.put("end", end.toString());
        obj.put("description", description);
        return obj;
    }
}
