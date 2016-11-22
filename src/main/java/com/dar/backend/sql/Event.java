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

    public static int insertEvent(String name, String url, String location, Date date, String description) throws NamingException, SQLException {
        String request = "INSERT INTO events (name, url, location, eventdate, description) VALUES (?,?,?,?,?) RETURNING id_event";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,name);
        stmt.setString(2, url);
        stmt.setString(3, location);
        stmt.setDate(4, date);
        stmt.setString(5, description);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        int id_trip = (Integer)res.get(0).get("id_event");
        conn.close();

        return id_trip;
    }

    public static int insertTag(int id_event, String id_category) throws NamingException, SQLException{
        System.out.println("DEBUG : ID_CAT = " + id_category + " ID_EVENT = " + id_event);
        System.out.flush();
        String request = "INSERT INTO tagged (id_event, id_category) SELECT ?, t.id_category FROM categories t WHERE t.name=?";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1, id_event);
        stmt.setString(2, id_category);
        int res = mngr.executeUpdate(stmt);
        conn.close();
        return res;
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
