package com.dar.backend.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.NamingException;
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
        String request = "SELECT * FROM Users WHERE id_user=?;";
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

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("Type", "User");
        obj.put("login", login);
        obj.put("Firstname", firstname);
        obj.put("Lastname", lastname);
        obj.put("Birthday", birthday.toString());
        obj.put("Country", country);
        obj.put("Email", email);
        obj.put("Description", description);
        return obj;
    }

    //TODO: that for every data type of the db
}
