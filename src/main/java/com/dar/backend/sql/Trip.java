package com.dar.backend.sql;

import org.json.simple.JSONObject;

public class Trip implements JSONable {

	@Override
	public JSONObject getJSON(long id) {
		JSONObject obj = new JSONObject();
		obj.put("From", NAME);
		obj.put("Date", DATE);
		obj.put("Context", TRIP);
		obj.put("Text", MESSAGE);
		return obj;
	}
	//Infos g�n�rales
	public static JSONObject getTrip(int id) {
		//Database call
		
		//JSON build
		
		return null;
	}
	//Retourne les X derniers messages de la conversation � partir de l'ID du voyage
	//Participants
	//Events s�lectionn�s

}