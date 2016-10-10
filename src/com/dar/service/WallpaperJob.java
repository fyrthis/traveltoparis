package com.dar.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import com.dar.api.APIRequestBuilder;
import com.dar.api.DeckChair;

public class WallpaperJob implements Runnable {

    //On devrait paramêtrer les args pour l'appel à l'api
    private BufferedImage wallpaper = null;
    private int height = 2160;
    private int width = 2*height;

    public static void main(String[] args) {
        System.out.println("Start test");
        new WallpaperJob().run();
        System.out.println("Done");
    }


    @Override
    public void run() {
        //Construct the api call
        String apicall = new APIRequestBuilder<DeckChair>()
                .addDomain(DeckChair.CAMERA)
                .addStr("5568862a7b28535025280c72")
                .addParam(DeckChair.WIDTH, width)
                .addParam(DeckChair.HEIGHT, height)
                .addParam(DeckChair.RESIZE, "fill")
                .addParam(DeckChair.GRAVITY, "South")
                .addParam(DeckChair.QUALITY, 100)
                .get();
        System.out.println(apicall);
        try {
            //URL url = new URL(apicall);

            URL url = new URL
                    ("http://images-generator.deckchair.com/57f92fd2d1b62be8429948bb?width=4320&height=2160&resizeMode=fill&gravity=South&quality=100");


            URL obj = url;
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            System.out.println("Request URL ... " + url);

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);

            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");
                // open the new connnection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();

                System.out.println("Redirect to URL : " + newUrl);
            }


            InputStream stream = url.openStream();
            int isDone = 0;
            byte[] data = new byte[1024];
            if(stream == null){
                throw new IOException("stream null !");
            }
            FileOutputStream test = new FileOutputStream("test.jpg");
            while (isDone!= -1) {
                isDone = stream.read(data);
                System.out.println("Data :" + isDone);

                test.write(data);
                test.flush();
            }
            test.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Si tout se passe bien, on devrait alors envoyer notre (nos ?) image(s) sur la base de donnée (Peut-on faire un systeme de cache ?)
        //Puis lorsqu'elles sont ajoutées, on supprime les anciennes (Attention, il doit y avoir un lock sur image, il ne faut pas qu'une
        // personne soit en train de télécharger l'image si on est en train de la supprimer !
    }
}