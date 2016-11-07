package com.dar.backend.sql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		String request = "SELECT * FROM Users WHERE id_user='"+id+";";
		ResultSet user = new SQLManager().executeQuery(request);
		this.id = user.getInt("id_user");
		this.login = user.getString("login");
		this.firstname = user.getString("firstname");
		this.lastname = user.getString("lastname");
		this.birthday = user.getDate("birthday");
		this.country = user.getString("country");
		this.email = user.getString("email");
		this.description = user.getString("description");
	}
	
	@Override
	public JSONObject getJSON() {
		JSONObject obj = new JSONObject();
		obj.put("Firstname", firstname);
		obj.put("Lastname", lastname);
		obj.put("Birthday", birthday.toString());
		obj.put("Email", email);
		obj.put("Description", description);
		return obj;
	}
	
	public static void main(String[] args) {
		int id = 0;
		try {
			System.out.println(new User(id).getJSON().toJSONString());
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
	}

}
