package com.cherryfish.stephanie.myfavoroutepics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Stephanie Verlingo on 7/21/2016.
 */
public class PhotoSearch extends AsyncTask<Void, Void, Boolean> {
    String apiURL= "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String apiKey="0f6f6c131f8eb464ded3ac9ada60bc00";
    Integer []farm=new Integer[10];
    String [] server = new String[10];
    String [] id = new String[10];
    String [] secret = new String[10];
    String [] photoURL = new String [10];
    Bitmap [] photoBitmaps = new Bitmap[10];
    String[] small = new String [10];
    String [] captions = new String[10];
    TravelList fragment;


    PhotoSearch(TravelList fragment){
        this.fragment=fragment;
    }
    @Override
//    https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=YOURAPIKEY&format=json&nojsoncallback=1&text=cats&extras=url_o
    protected Boolean doInBackground(Void... params) {

            //Create JSON object for post
        try {
            String url = apiURL + "&api_key=" + apiKey+"&format=json&nojsoncallback=1&text=" + "thailand"+ "&extras=url_n";
            JSONObject results = postRequest(url);
//            String message = (String)jsnobject.get("Message");
            System.out.println(results.toString());
            JSONObject object = (JSONObject) results.get("photos");
            JSONArray photos = (JSONArray) object.get("photo");
            System.out.println("size is " + photos.length());
            for (int i=0;i<10;i++) {
                System.out.println("i is "+ i);
                JSONObject photo = (JSONObject) photos.get(i);
                farm[i] = (Integer) photo.get("farm");
                server[i] = (String) photo.get("server");

                id[i] = (String) photo.get("id");
                secret[i] = (String) photo.get("secret");
                photoURL[i] = "http://farm" + farm[i] + ".staticflickr.com/" + server[i] + "/" + id[i] + "_" + secret[i] + ".jpg";

                small[i] = (String) photo.get("url_n");
                photoBitmaps[i]= urlStringToBitmap(small[i]);
                captions[i]= (String) photo.get("title");
                System.out.println(captions[i]);

            }
            return true;
            } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }


    public JSONObject postRequest(String url) {


        URL route = null;
        HttpURLConnection client = null;

        try {
            //Create URL route
            route = new URL(url);


            //open connection and ready it for post request
            client = (HttpURLConnection) route.openConnection();
            client.setRequestMethod("GET");
            client.connect();
            //receive results
            int HttpResult = client.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println("" + sb.toString());
            JSONObject jsnobject = new JSONObject(sb.toString());
            return jsnobject;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap urlStringToBitmap(String s){
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(s).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    public void onPostExecute(Boolean result){
        fragment.displayData(photoBitmaps, captions);
    }
}
