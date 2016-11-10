package com.dar.backend.sql;

import org.json.simple.JSONObject;

public class Event implements JSONable {
	/*
	 *   id_event serial not null,
  name VARCHAR(100),
  url VARCHAR(512),
  location VARCHAR(100),
  eventdate DATE,
  picture bytea,
  description text,
  PRIMARY KEY(id_event)
	 */
	// "SELECT * FROM Events WHERE id='"+id+";"
	public Event(String id) {
		
	}
	
	
	@Override
	public JSONObject getJSON(long id) {
		JSONObject obj = new JSONObject();
		obj.put("Title", NAME);
		obj.put("Location", DATE);
		obj.put("URL", TRIP);
		obj.put("Description", MESSAGE);
		obj.put("Date", MESSAGE);
		obj.put("Picture", MESSAGE);
		return obj;
	}

}