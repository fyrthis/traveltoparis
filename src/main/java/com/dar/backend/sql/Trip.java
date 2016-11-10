package com.dar.backend.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Trip implements JSONable {
    private int id;
    private String name;
    private String description;
    private Date begins;
    private Date ends;

    public Trip(int id) throws NamingException, SQLException {
        String request = "SELECT * FROM trips WHERE id_trip=?;";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        conn.close();
        HashMap<String, Object> first = res.get(0);
        this.id = (Integer)first.get("id_trip");
        this.name = (String)first.get("name");
        this.description = (String)first.get("description");
        this.begins = (Date)first.get("begins");
        this.ends = (Date)first.get("ends");
    }

    public Trip(int id, String name, String description, Date begins, Date ends){
        this.id = id;
        this.name = name;
        this.description = description;
        this.begins = begins;
        this.ends = ends;
    }

    public JSONObject getTripEvents() throws NamingException, SQLException {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        String request = "SELECT v.is_like, u.login, c.description AS description_cat, e.* FROM trips t, votes v, users u, events e, tagged tg, categories c " +
                "WHERE t.id_trip = ? AND v.id_trip=t.id_trip AND v.id_event=e.id_event AND v.id_user=u.id_user AND e.id_event=tg.id_event " +
                "AND tg.id_category = c.id_category";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            JSONObject elem = new JSONObject();
            elem.put("user", e.get("login"));
            elem.put("is_like", e.get("is_like"));
            elem.put("category", e.get("description_cat"));
            Event event = new Event((Integer)e.get("id_event"),
                    (String)e.get("name"),
                    (String)e.get("url"),
                    (String)e.get("location"),
                    (Date)e.get("date"),
                    (String)e.get("description"));
            elem.put("event", event.getJSON());
            arr.add(elem);
        }
        obj.put("size", res.size());
        obj.put("list", arr);
        conn.close();
        return obj;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", "trip");
        obj.put("id", id);
        obj.put("name", name);
        obj.put("description", description);
        obj.put("begins", begins.toString());
        obj.put("ends", ends.toString());
        return obj;
    }
}
