package com.dar.service;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.dar.api.APIRequestBuilder;
import com.dar.api.DeckChair;

public class WallpaperJob implements Runnable {
	
	//On devrait paramêtrer les args pour l'appel à l'api
	private Image wallpaper = null;
	
	@Override
	public void run() {
		//Construct the api call
		String apicall = new APIRequestBuilder<DeckChair>()
				.addDomain(DeckChair.CAMERA)
				.addStr("57f56a4e3c7bf34e4274f415")
				.addParam(DeckChair.WIDTH, "800")
				.addParam(DeckChair.HEIGHT, "600")
				.get();

		try {
		    URL url = new URL(apicall.toString());
		    wallpaper = ImageIO.read(url);
		} catch (IOException e) {
			return;
		}
		//Si tout se passe bien, on devrait alors envoyer notre (nos ?) image(s) sur la base de donnée (Peut-on faire un systeme de cache ?)
		//Puis lorsqu'elles sont ajoutées, on supprime les anciennes (Attention, il doit y avoir un lock sur image, il ne faut pas qu'une 
		// personne soit en train de télécharger l'image si on est en train de la supprimer !
	}
	
	public Image get() { //PAS DE GET, on devrait directement SET l'image dans le run() quelque part (BDD, cache, ...)
		return wallpaper;
	}
}