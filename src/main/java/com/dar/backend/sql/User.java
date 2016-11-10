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
            elem.put("Trip", trip.getJSON());
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
        obj.put("Type", "user");
        obj.put("Login", login);
        obj.put("Firstname", firstname);
        obj.put("Lastname", lastname);
        obj.put("Birthday", birthday.toString());
        obj.put("Country", country);
        obj.put("Email", email);
        obj.put("Description", description);
        return obj;
    }
}
