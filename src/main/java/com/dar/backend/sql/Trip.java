package com.dar.backend.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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
        HashMap<String, Object> first = res.get(0);
        this.id = (Integer)first.get("id_trip");
        this.name = (String)first.get("name");
        this.description = (String)first.get("description");
        this.begins = (Date)first.get("begins");
        this.ends = (Date)first.get("ends");
        conn.close();
    }

    public Trip(int id, String name, String description, Date begins, Date ends){
        this.id = id;
        this.name = name;
        this.description = description;
        this.begins = begins;
        this.ends = ends;
    }

    public JSONObject chooseNewEvents(String[] categories, Date start, Date end, String sort) throws NamingException, SQLException{
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        StringBuilder builder = new StringBuilder("SELECT e.* FROM events e, categories c, tagged t WHERE e.id_event=t.id_event AND ");
        if(categories != null) {
            for (int i = 0; i < categories.length; i++) {
                builder.append("(c.id_category=? AND c.id_category=t.id_category)");
                if (i == categories.length - 1) builder.append(" AND ");
                else builder.append(" OR ");
            }
        }
        builder.append("e.eventbegin => ? AND e.eventend <= ? ");
        if(sort.equals("Date")) builder.append("ORDER BY e.eventbegin, e.eventend");
        if(sort.equals("Category")) builder.append("ORDER BY c.id_category");
        //String request = "SELECT e.* FROM events e, categories c, tagged t WHERE e.id_event=t.id_event AND c.name=? AND c.id_category=t.id_category AND e.eventbegin => ? AND e.eventend <= ? ORDER BY c.id_category";
        String request = builder.toString();
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        int i = 1;
        if(categories != null) {
            for(String s : categories){
                stmt.setString(i++, s);
            }
        }
        stmt.setDate(i++, start);
        stmt.setDate(i, end);
        System.out.println("REQ : " + stmt.toString());
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            JSONObject elem = new JSONObject();
            elem.put("id", e.get("id_event"));
            elem.put("name", e.get("name"));
            elem.put("url", e.get("url"));
            elem.put("location", e.get("location"));
            elem.put("begins", e.get("eventbegin"));
            elem.put("ends", e.get("eventend"));
            elem.put("description", e.get("description"));
            arr.add(elem);
        }
        conn.close();
        obj.put("list", arr);
        obj.put("size", res.size());
        return obj;
    }

    public JSONObject getTripEvents() throws NamingException, SQLException {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        String request = "SELECT v.is_like, u.login, c.description AS description_cat, e.* FROM votes v, users u, events e, tagged tg, categories c " +
                "WHERE v.id_trip=? AND v.id_event=e.id_event AND v.id_user=u.id_user AND e.id_event=tg.id_event AND tg.id_category = c.id_category;";
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
                    (Date)e.get("eventbegin"),
                    (Date)e.get("eventend"),
                    (String)e.get("description"));
            elem.put("event", event.getJSON());
            arr.add(elem);
        }
        obj.put("size", res.size());
        obj.put("list", arr);
        conn.close();
        return obj;
    }

    public JSONObject getTripOverview() throws NamingException, SQLException {
        JSONObject obj = new JSONObject();
        String request = "SELECT i.id_user FROM involded i WHERE i.id_trip=?";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1, id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        obj.put("participants", res.size());
        request = "SELECT e.id_event, c.name AS cat_name FROM events e, votes v, tagged t, categories c" +
                " WHERE v.id_trip=? AND e.id_event=v.id_event AND t.id_event=e.id_event AND c.id_category = t.id_category;";
        stmt = conn.prepareStatement(request);
        stmt.setInt(1, id);
        HashSet<Integer> id_event_list = new HashSet<>();
        HashMap<String, Integer> events_in_cat = new HashMap<>();
        res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            Integer id_event = (Integer)e.get("id_event");
            if(!id_event_list.contains(id_event)) {
                id_event_list.add(id_event);
                String cat_name = (String)e.get("cat_name");
                events_in_cat.put(cat_name, events_in_cat.get(cat_name) + 1);
            }
        }
        obj.put("name", this.name);
        obj.put("description", this.description);
        obj.put("begins", this.begins.toString());
        obj.put("ends", this.ends.toString());
        obj.put("events", id_event_list.size());
        obj.put("categories", events_in_cat.size());
        Iterator it = events_in_cat.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            obj.put(pair.getKey(), pair.getValue());
            it.remove();
        }
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
