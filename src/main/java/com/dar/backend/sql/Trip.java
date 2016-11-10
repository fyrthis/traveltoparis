package com.dar.backend.sql;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

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
        String request = "SELECT v.is_like, u.login, e.* FROM trips t, votes v, users u, events e WHERE t.id_trip = ? AND v.id_trip=t.id_trip " +
                "AND v.id_event=e.id_event AND v.id_user=u.id_user";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            JSONObject elem = new JSONObject();
            elem.put("User", e.get("login"));
            elem.put("Is_like", e.get("is_like"));
            Event event = new Event((Integer)e.get("id_event"),
                    (String)e.get("name"),
                    (String)e.get("url"),
                    (String)e.get("location"),
                    (Date)e.get("date"),
                    (String)e.get("description"));
            elem.put("Event", event.getJSON());
            arr.add(elem);
        }
        obj.put("Size", res.size());
        obj.put("List", arr);
        conn.close();
        return obj;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("Name", name);
        obj.put("Description", description);
        obj.put("Begins", begins.toString());
        obj.put("Ends", ends.toString());
        return obj;
    }
}
