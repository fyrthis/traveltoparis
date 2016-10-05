package com.dar.service;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class WallpaperJob implements Runnable {
	
	//On devrait paramêtrer les args pour l'appel à l'api
	private String webcam = null;
	
	public WallpaperJob(String webcam) {
		this.webcam = webcam;
	}
	@Override
	public void run() {
		//Construct the api call
		StringBuilder apicall = new StringBuilder();
		apicall.append("http://api.deckchair.com/v1/viewer/image/").append(webcam).append("?width=").append("1800").append("&height=").append("900");
		//Get the image through the api call
		if(webcam==null) return;
		Image image = null;
		try {
		    URL url = new URL(apicall.toString());
		    image = ImageIO.read(url);
		} catch (IOException e) {
			return;
		}
		//Si tout se passe bien, on devrait alors envoyer notre (nos ?) image(s) sur la base de donnée (Peut-on faire un systeme de cache ?)
		//Puis lorsqu'elles sont ajoutées, on supprime les anciennes (Attention, il doit y avoir un lock sur image, il ne faut pas qu'une 
		// personne soit en train de télécharger l'image si on est en train de la supprimer !
	}
}