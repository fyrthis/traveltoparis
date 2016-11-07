package com.dar.backend.sql;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Message implements JSONable {

	//Retourne le message en lui-même
	@Override
	public JSONObject getJSON(long id) {
		JSONObject obj = new JSONObject();
		obj.put("From", NAME);
		obj.put("Date", DATE);
		obj.put("Context", TRIP);
		obj.put("Text", MESSAGE);
		return obj;
	}
	
	
	
	

}
