package com.example.thierry.archiverts;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wengerol on 20.06.2017.
 */

public final class Tools extends Activity {
    private Context context;

    // parse the JSON repsonse of API to create object Article
    public static ArrayList<Article> responseApiJSONtoListArticles(String response)
    {
        // Reset listArticles
        ArrayList<Article> articles = new ArrayList<Article>();

        /****************** Start Parse Response JSON Data *************/
        String OutputData = "";
        JSONObject jsonResponse;

        try {

            JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
            JSONObject json1 = json.getJSONObject("response");
            int numFound = (Integer) json1.get("numFound");
            int start = (Integer) json1.get("start");
            JSONArray docs = json1.getJSONArray("docs");

            Log.i("1:", "numFound : " + numFound + ", start: " + start);

            int lengthJsonArr = docs.length();
            String program;
            String publicationDate;
            String title;
            String imageURL;
            String mediaURL;
            String excerpt;
            String mediaType;
            String articleURL;
            int duration;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date;

            for (int i = 0; i < lengthJsonArr; i++) {
                /****** Get Object for each JSON node.***********/
                try {
                    program = (String) (docs.getJSONObject(i).get("program"));
                } catch (Exception e) {
                    program = "";
                }
                try {
                    title = (String) (docs.getJSONObject(i).get("title"));
                } catch (Exception e) {
                    title = "";
                }
                date = new Date();
                try {
                    publicationDate = (String) (docs.getJSONObject(i).get("publicationDate"));
                    date = formatter.parse(publicationDate.replaceAll("Z$", "+0000"));
                } catch (Exception e) {
                    publicationDate = "";
                }
                try {
                    imageURL = (String) (docs.getJSONObject(i).get("imageURL"));
                } catch (Exception e) {
                    imageURL = "";
                }
                try {
                    mediaURL = (String) (docs.getJSONObject(i).get("mediaURL"));
                } catch (Exception e) {
                    mediaURL = "";
                }
                try {
                    excerpt = (String) (docs.getJSONObject(i).get("excerpt"));
                } catch (Exception e) {
                    excerpt = "";
                }
                try {
                    mediaType = (String) (docs.getJSONObject(i).get("mediaType"));
                } catch (Exception e) {
                    mediaType = "";
                }
                try {
                    duration = (int) (docs.getJSONObject(i).get("durationSec"));
                } catch (Exception e) {
                    duration = 0;
                }
                Log.i(i + ":", "titre d'émission:" + program + " titre: " + title + " Date de publication: " + publicationDate + "Résumé: " + excerpt);
                Log.i(i + ":", "image URL:" + imageURL + " media URL: " + mediaURL);

                OutputData += " program          : " + program;
                articles.add(new Article(title, program, excerpt, date, mediaType, duration, imageURL, mediaURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return articles;
    }
}
