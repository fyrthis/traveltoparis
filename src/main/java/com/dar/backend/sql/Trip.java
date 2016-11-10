package com.dar.backend.sql;

import org.json.simple.JSONObject;

public class Trip implements JSONable {
/*
 *   id_trip serial not null,
  name VARCHAR(100),
  picture bytea,
  description text,
  begins date,
  ends date,
  PRIMARY KEY (id_trip)
 */
	@Override
	public JSONObject getJSON(long id) {
		JSONObject obj = new JSONObject();
		obj.put("From", NAME);
		obj.put("Date", DATE);
		obj.put("Context", TRIP);
		obj.put("Text", MESSAGE);
		return obj;
	}
	//Infos générales
	public static JSONObject getTrip(int id) {
		//Database call
		
		//JSON build
		
		return null;
	}
	//Retourne les X derniers messages de la conversation à partir de l'ID du voyage
	//Participants
	//Events sélectionnés

}
