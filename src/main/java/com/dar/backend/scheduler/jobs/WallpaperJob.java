package com.dar.backend.scheduler.jobs;


import com.dar.backend.api.APIRequestBuilder;
import com.dar.backend.api.DeckChair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;


public class WallpaperJob implements Runnable {

    private static final int native4kHeight = 3000;
    private static final int native4kWidth = 2*native4kHeight;
    // TODO remplacer le path actuel par un path qui marche
    private static final String imagesPath = "/opt/tomcat/webres/";
    //private static final String imagesPath = "resources/";
    private String cameraID;

    public WallpaperJob(String camString){
        cameraID = camString;
    }

    @Override
    public void run() {
        try {
            System.out.println(new Date().toString() + " | Launching background routine");
            String apicall = new APIRequestBuilder<DeckChair>()
                    .addDomain(DeckChair.CAMERA)
                    .addStr(cameraID)
                    .addParam(DeckChair.WIDTH, native4kWidth)
                    .addParam(DeckChair.HEIGHT, native4kHeight)
                    .addParam(DeckChair.RESIZE, "fill")
                    .addParam(DeckChair.GRAVITY, "South")
                    .addParam(DeckChair.QUALITY, 100)
                    .get();
            String redirect = apicall;
            while (apicall != null) {
                apicall = getRedirect(apicall);
                if (apicall != null) redirect = apicall;
            }
            writeImage(redirect, imagesPath + "original.jpg");
            resizeImage(3840, 2160);
            resizeImage(2560, 1440);
            resizeImage(1920, 1080);
            resizeImage(1366, 768);
            System.out.println(new Date().toString() + " | Done");
        } catch (Exception e){
            e.printStackTrace(System.out);
            System.out.println(new Date().toString() + " | Not done");
            e.printStackTrace();
        }
    }

    private String getRedirect(String urlString){
        try {
            boolean redirect = false;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            if (redirect) {
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
        File file = new File(fileName);
        /*if(!file.setReadable(true, false) ||
                !file.setExecutable(true, false) ||
                !file.setWritable(true, false)) throw new IOException("Couldn't set permissions");*/
        OutputStream os = new FileOutputStream(file);
        byte[] data = new byte[2048];
        int length;
        while ((length = is.read(data)) != -1) {
            os.write(data, 0, length);
        }
        is.close();
        os.close();
    }

    private static void resizeImage(int imgWidth, int imgHeight){
        try {
            File file = new File(imagesPath + "original.jpg");
            /*if(!file.setReadable(true, false) ||
            !file.setExecutable(true, false) ||
            !file.setWritable(true, false)) throw new IOException("Couldn't set permissions");*/
            BufferedImage originalImage = ImageIO.read(file);
            //int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizedImage = getScaledInstance(originalImage, imgWidth, imgHeight,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
            File output = new File(imagesPath + imgWidth + "x" + imgHeight + ".jpg");
            ImageIO.write(resizedImage,"jpg", output);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
                                                   boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }
            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);
        return ret;
    }
}