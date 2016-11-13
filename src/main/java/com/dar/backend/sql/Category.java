package com.dar.backend.sql;

import org.json.simple.JSONObject;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Category implements JSONable{
    private int id;
    private String name;
    private String description;


    public Category(int id) throws NamingException, SQLException {
        String request = "SELECT * FROM categories WHERE id_category=?;";
        SQLManager mngr = new SQLManager();
        Connection conn = mngr.getConnection();
        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1,id);
        ArrayList<HashMap<String, Object>> res = mngr.executeQuery(stmt);
        conn.close();
        HashMap<String, Object> first = res.get(0);
        this.id = (Integer)first.get("id_category");
        this.name = (String)first.get("name");
        this.description = (String)first.get("description");
    }

    @Override
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", "category");
        obj.put("name", name);
        obj.put("description", description);
        return obj;
    }
}
