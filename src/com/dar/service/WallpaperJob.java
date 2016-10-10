package com.dar.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


import com.dar.api.APIRequestBuilder;
import com.dar.api.DeckChair;

public class WallpaperJob implements Runnable {

    private int native4kHeight = 2160;
    private int native4kWidth = 2*native4kHeight;

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
                .addParam(DeckChair.WIDTH, native4kWidth)
                .addParam(DeckChair.HEIGHT, native4kHeight)
                .addParam(DeckChair.RESIZE, "fill")
                .addParam(DeckChair.GRAVITY, "South")
                .addParam(DeckChair.QUALITY, 100)
                .get();
        String redirect = apicall;
        while (apicall != null){
            apicall = getRedirect(apicall);
            if(apicall != null){
                redirect = apicall;
            }
        }
        try {
            writeImage(redirect, "test.jpg");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private String getRedirect(String urlString){
        try {
            boolean redirect = false;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //System.out.println("Request URL ... " + url);
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            System.out.println("Response Code ... " + status);
            if (redirect) {
                //String newUrl = conn.getHeaderField("Location");
                //System.out.println("Redirect to URL : " + newUrl);
                //return newUrl;
                return conn.getHeaderField("Location");
            }
            return null;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static void writeImage(String imageURL, String fileName) throws IOException{
        URL url = new URL(imageURL);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(fileName);
        byte[] data = new byte[2048];
        int length;
        //int cumul = 0;
        while ((length = is.read(data)) != -1) {
            //System.out.println("Read : " + cumul + " bytes");
            //cumul += length;
            os.write(data, 0, length);
        }
        is.close();
        os.close();
    }
}