package com.dar.backend.sql;

import org.json.simple.JSONObject;

public class Event implements JSONable {

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