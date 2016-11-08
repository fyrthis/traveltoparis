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

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("Type", "event");
        obj.put("Name", name);
        obj.put("Url", url);
        obj.put("Location", location);
        obj.put("Date", date.toString());
        obj.put("Description", description);
        return obj;
    }
}
