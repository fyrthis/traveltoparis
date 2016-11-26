package com.dar.backend.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class User implements JSONable {

    private int id;
    private String login;
    private String firstname;
    private String lastname;
    private Date birthday;
    private String country;
    private String email;
    //private Image picture;
    private String description;

    public User(int id) throws NamingException, SQLException {
        String request = "SELECT * FROM users WHERE id_user=?;";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        conn.close();
        HashMap<String, Object> first = res.get(0);
        this.id = (Integer)first.get("id_user");
        this.login = (String)first.get("login");
        this.firstname = (String)first.get("firstname");
        this.lastname = (String)first.get("lastname");
        this.birthday = (Date)first.get("birthday");
        this.country = (String)first.get("country");
        this.email = (String)first.get("email");
        this.description = (String)first.get("description");
    }

    public User(String uname) throws NamingException, SQLException{
        String request = "SELECT * FROM Users WHERE login=?;";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,uname);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        conn.close();
        HashMap<String, Object> first = res.get(0);
        this.id = (Integer)first.get("id_user");
        this.login = (String)first.get("login");
        this.firstname = (String)first.get("firstname");
        this.lastname = (String)first.get("lastname");
        this.birthday = (Date)first.get("birthday");
        this.country = (String)first.get("country");
        this.email = (String)first.get("email");
        this.description = (String)first.get("description");
    }

    public JSONObject getUserTrips() throws NamingException, SQLException {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        String request = "SELECT t.*, i.is_admin FROM involded i, trips t, users u WHERE u.id_user=? AND i.id_user=u.id_user " +
                "AND t.id_trip=i.id_trip AND i.is_admin=TRUE;";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        for(HashMap<String, Object> e : res){
            JSONObject elem = new JSONObject();
            elem.put("admin", e.get("is_admin").toString());
            Trip trip = new Trip((Integer) e.get("id_trip"),
                    (String) e.get("name"),
                    (String) e.get("description"),
                    (Date) e.get("begins"),
                    (Date) e.get("ends"));
            elem.put("trip", trip.getJSON());
            arr.add(elem);
        }
        obj.put("size", res.size());
        obj.put("list", arr);
        conn.close();
        return obj;
    }

    public void addEventToTrip(int id_trip, String id_event, boolean is_like) throws NamingException, SQLException{
        String request = "INSERT INTO votes (id_user, id_event, id_trip, is_like) SELECT ?, ?, ?, ? WHERE exists(SELECT 1 FROM involded i WHERE i.id_user=? AND i.id_trip=?) " +
                "AND NOT exists(SELECT 1 FROM votes v WHERE v.id_event=? AND v.id_trip=? AND v.id_user=?)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1, this.id);
        stmt.setString(2, id_event);
        stmt.setInt(3, id_trip);
        stmt.setBoolean(4, is_like);
        stmt.setInt(5, this.id);
        stmt.setInt(6, id_trip);
        stmt.setString(7, id_event);
        stmt.setInt(8, id_trip);
        stmt.setInt(9, this.id);
        mngr.executeUpdate(stmt);
        conn.close();
    }

    public void removeEventFromTrip(int id_trip, String id_event) throws NamingException, SQLException{
        String request = "DELETE FROM votes v WHERE v.id_trip=? AND v.id_event=? AND exists(SELECT 1 FROM involded i WHERE i.id_trip=v.id_trip AND i.id_user=?)";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1, id_trip);
        stmt.setString(2, id_event);
        stmt.setInt(3, this.id);
        mngr.executeUpdate(stmt);
        conn.close();
    }

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", "user");
        obj.put("id", id);
        obj.put("login", login);
        obj.put("firstname", firstname);
        obj.put("lastname", lastname);
        obj.put("birthday", birthday.toString());
        obj.put("country", country);
        obj.put("email", email);
        obj.put("description", description);
        return obj;
    }
}
