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
        if(res.size() > 0) {
            HashMap<String, Object> first = res.get(0);
            this.id = (Integer) first.get("id_trip");
            this.name = (String) first.get("name");
            this.description = (String) first.get("description");
            this.begins = (Date) first.get("begins");
            this.ends = (Date) first.get("ends");
        }
        else{
            this.id=-1;
        }
        conn.close();
    }

    public Trip(int id, String name, String description, Date begins, Date ends){
        this.id = id;
        this.name = name;
        this.description = description;
        this.begins = begins;
        this.ends = ends;
    }

    public JSONObject chooseNewEvents(String[] categories, Date start, Date end, String sort, int page) throws NamingException, SQLException{
        int pagesize = 20;
    	JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        StringBuilder builder = new StringBuilder("SELECT DISTINCT e.* FROM events e, categories c, tagged t WHERE e.id_event=t.id_event AND ");
        if(categories != null) {
            builder.append("(");
            for (int i = 0; i < categories.length; i++) {
                builder.append("(c.name=? AND c.id_category=t.id_category)");
                if (i == categories.length - 1) builder.append(") AND ");
                else builder.append(" OR ");
            }
        }
        builder.append("e.eventbegin >= ? AND e.eventend <= ? AND NOT exists(SELECT 1 FROM votes v WHERE v.id_event = e.id_event AND v.id_trip=?)");
        if(sort.equals("Date")) builder.append("ORDER BY e.eventbegin, e.eventend");
        if(sort.equals("Category")) builder.append("ORDER BY c.id_category");
        builder.append(" LIMIT ").append(pagesize).append(" OFFSET ").append(page*pagesize);
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
        stmt.setDate(i++, end);
        stmt.setInt(i, id);
        System.out.println("REQ : " + stmt.toString());
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            JSONObject elem = new JSONObject();
            elem.put("id", e.get("id_event"));
            elem.put("name", e.get("name"));
            elem.put("url", e.get("url"));
            elem.put("location", e.get("location"));
            elem.put("begins", e.get("eventbegin").toString());
            elem.put("ends", e.get("eventend").toString());
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
        String request = "SELECT e.*, sum((v.is_like = TRUE)::INT) AS upvotes, sum((v.is_like = FALSE)::int) AS downvotes " +
                "FROM events e, votes v, trips t WHERE e.id_event=v.id_event AND v.id_trip=? GROUP BY e.id_event";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            JSONObject elem = new JSONObject();
            Event event = new Event((String)e.get("id_event"),
                    (String)e.get("name"),
                    (String)e.get("url"),
                    (String)e.get("location"),
                    (Date)e.get("eventbegin"),
                    (Date)e.get("eventend"),
                    (String)e.get("description"));
            elem.put("event", event.getJSON());
            elem.put("upvotes", e.get("upvotes"));
            elem.put("downvotes", e.get("downvotes"));
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
        request = "SELECT DISTINCT e.id_event,\n" +
                "  sum((c.id_category=1)::INT) AS music,\n" +
                "  sum((c.id_category=2)::INT) AS family,\n" +
                "  sum((c.id_category=3)::INT) AS food,\n" +
                "  sum((c.id_category=4)::INT) AS movie,\n" +
                "  sum((c.id_category=5)::INT) AS art,\n" +
                "  sum((c.id_category=6)::INT) AS support,\n" +
                "  sum((c.id_category=7)::INT) AS attraction,\n" +
                "  sum((c.id_category=8)::INT) AS sports,\n" +
                "  sum((c.id_category=9)::INT) AS technology,\n" +
                "  sum((c.id_category=10)::INT) AS festival,\n" +
                "  sum((c.id_category=11)::INT) AS fundraiser,\n" +
                "  sum((c.id_category=12)::INT) AS animals\n" +
                "FROM trips t, events e, votes v, tagged tag, categories c\n" +
                "WHERE t.id_trip=? AND e.id_event=v.id_event AND v.id_trip=t.id_trip AND tag.id_event=e.id_event AND c.id_category=tag.id_category\n" +
                "GROUP BY e.id_event";
        stmt = conn.prepareStatement(request);
        stmt.setInt(1, id);
        res = mngr.executeQuery(stmt);
        HashMap<String, Integer> cat_num = new HashMap<>();
        cat_num.put("music", 0);
        cat_num.put("family", 0);
        cat_num.put("food", 0);
        cat_num.put("movie", 0);
        cat_num.put("art", 0);
        cat_num.put("support", 0);
        cat_num.put("attraction", 0);
        cat_num.put("sports", 0);
        cat_num.put("technology", 0);
        cat_num.put("festival", 0);
        cat_num.put("fundraiser", 0);
        cat_num.put("animals", 0);
        for(HashMap<String, Object> e : res){
            if((Long)e.get("music") > 0)cat_num.put("music", cat_num.get("music")+1);
            if((Long)e.get("family") > 0)cat_num.put("family", cat_num.get("family")+1);
            if((Long)e.get("food") > 0)cat_num.put("food", cat_num.get("food")+1);
            if((Long)e.get("movie") > 0)cat_num.put("movie", cat_num.get("movie")+1);
            if((Long)e.get("art") > 0)cat_num.put("art", cat_num.get("art")+1);
            if((Long)e.get("support") > 0)cat_num.put("support", cat_num.get("support")+1);
            if((Long)e.get("attraction") > 0)cat_num.put("attraction", cat_num.get("attraction")+1);
            if((Long)e.get("sports") > 0)cat_num.put("sports", cat_num.get("sports")+1);
            if((Long)e.get("technology") > 0)cat_num.put("technology", cat_num.get("technology")+1);
            if((Long)e.get("festival") > 0)cat_num.put("festival", cat_num.get("festival")+1);
            if((Long)e.get("fundraiser") > 0)cat_num.put("fundraiser", cat_num.get("fundraiser")+1);
            if((Long)e.get("animals") > 0)cat_num.put("animals", cat_num.get("animals")+1);
        }
        obj.put("name", this.name);
        obj.put("description", this.description);
        obj.put("begins", this.begins.toString());
        obj.put("ends", this.ends.toString());
        obj.put("events", res.size());
        Iterator it = cat_num.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            obj.put(pair.getKey(), pair.getValue());
            it.remove();
        }
        conn.close();
        return obj;
    }

    public JSONObject getTripParticipants() throws NamingException, SQLException{
        String request = "SELECT DISTINCT u.login FROM users u, involded i WHERE i.id_trip=? AND users.id_user=i.id_user";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        conn.close();
    }

    public static boolean checkIfUserIsInTrip(int id_trip, String uname) throws NamingException, SQLException{
        String request = "SELECT exists(SELECT 1 FROM involded i, users u WHERE u.login=? AND i.id_user=u.id_user AND id_trip=?)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, uname);
        stmt.setInt(2, id_trip);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        conn.close();
        return (boolean)res.get(0).get("exists");
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


    public int getId(){return id;}
}
