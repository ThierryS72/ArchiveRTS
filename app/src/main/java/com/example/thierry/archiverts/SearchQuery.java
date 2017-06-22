package com.example.thierry.archiverts;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thierry on 22.06.2017.
 */

public class SearchQuery extends MainActivity {

    private ListView mListView;
    private List<Article> articles = resetArticles();
    ArticleAdapter adapter;
    private String searchStringQuery = "";

    EditText searchString;

    private List<Article> resetArticles()
    {
        List<Article> articles = new ArrayList<Article>();
        return articles;
    }

    public int SearchQuery() {
        // WebServer Request URL
        searchString = (EditText) findViewById(R.id.searchString);
        searchStringQuery =searchString.getText().toString();

        String apiKey = getString(R.string.apikey);
        String apiServerUrl = getString(R.string.api_url);
        String serverURL = apiServerUrl + "?apikey=" + apiKey + "&query=" + searchStringQuery + "&enumeratedFacets=mediaType&rows=100&sort=-score%2C-isDuplicate%2C-publicationDate";

        Log.i("Search URL : ",serverURL);
        Log.i("Search keyword : ",searchStringQuery);

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
        return 1;
    }
    private class LongOperation extends AsyncTask<String, Void, Void> {

        // Required initialization
        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        //private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        private ProgressDialog Dialog = new ProgressDialog(SearchQuery.this);
        String data ="";
        TextView uiUpdate = (TextView) findViewById(R.id.output);
        TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;
        EditText searchString = (EditText) findViewById(R.id.searchString);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.
            //Start Progress Dialog (Message)
            Dialog.setMessage("Patientez...");
            Dialog.show();

            try{
                // Set Request parameter
                data +="&" + URLEncoder.encode("data", "UTF-8") + "="+searchString.getText();
                searchStringQuery = searchString.getText().toString();

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch(Exception ex) {}
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
            // Close progress dialog

            // Liste d'article
            //List<Article> listArticles = new ArrayList<Article>();

            Dialog.dismiss();

            if (Error != null) {

                uiUpdate.setText("Output : "+Error);
                Toast.makeText(getApplicationContext(), "Une erreur est survenue..", Toast.LENGTH_SHORT).show();

            } else {

                // Show Response Json On Screen (activity)
                uiUpdate.setText( Content );

                // Reset listArticles
                articles = resetArticles();

                /****************** Start Parse Response JSON Data *************/
                String OutputData = "";
                JSONObject jsonResponse;

                try {

                    JSONObject json = (JSONObject) new JSONTokener(Content).nextValue();
                    JSONObject json1 = json.getJSONObject("response");
                    //JSONObject json3 = json.getJSONObject("0");
                    int numFound = (Integer) json1.get("numFound");
                    int start = (Integer) json1.get("start");
                    JSONArray docs = json1.getJSONArray("docs");

                    Log.i("1:", "numFound : " + numFound +", start: " +start);

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

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/


                        /******* Fetch node values **********/
                        try {
                            program = (String) (docs.getJSONObject(i).get("program"));
                        }
                        catch(Exception e)
                        {
                            program = "";
                        }
                        try{
                            title = (String) (docs.getJSONObject(i).get("title"));
                        }
                        catch(Exception e)
                        {
                            title="";
                        }
                        try{
                            imageURL = (String) (docs.getJSONObject(i).get("imageURL"));
                        }
                        catch(Exception e)
                        {
                            imageURL="";
                        }
                        try{
                            articleURL = (String) (docs.getJSONObject(i).get("mediaURL"));
                        }
                        catch(Exception e)
                        {
                            articleURL="";
                        }
                        date = new Date();
                        try{
                            publicationDate = (String) (docs.getJSONObject(i).get("publicationDate"));
                            date = formatter.parse(publicationDate.replaceAll("Z$", "+0000"));
                        }
                        catch(Exception e){
                            publicationDate = "";
                        }
                        try{
                            imageURL = (String) (docs.getJSONObject(i).get("imageURL"));
                        }
                        catch(Exception e)
                        {
                            imageURL = "";
                        }
                        try{
                            mediaURL = (String) (docs.getJSONObject(i).get("mediaURL"));
                        }
                        catch(Exception e)
                        {
                            mediaURL = "";
                        }
                        try{
                            excerpt = (String) (docs.getJSONObject(i).get("excerpt"));
                        }
                        catch(Exception e){
                            excerpt = "";
                        }
                        try {
                            mediaType = (String) (docs.getJSONObject(i).get("mediaType"));
                        }
                        catch(Exception e)
                        {
                            mediaType = "";
                        }
                        try {
                            duration = (int) (docs.getJSONObject(i).get("durationSec"));
                        }
                        catch(Exception e)
                        {
                            duration = 0;
                        }
                        Log.i(i +":", "titre d'émission:" + program + " titre: " + title + " Date de publication: " + publicationDate + "Résumé: " + excerpt );
                        Log.i(i +":", "image URL:" + imageURL + " media URL: " + mediaURL );

                        OutputData += " program          : "+ program;

                        // add article to articleList
                        // if(excerpt.length() > 100) excerpt = excerpt.substring(0,100) + "...";
                        articles.add(new Article(title,program,excerpt,date,mediaType,duration,imageURL,mediaURL));
                    }
                    // update ListView
                    mListView.destroyDrawingCache();
                    adapter.clear();
                    adapter.addAll(articles);
                    adapter.notifyDataSetChanged();
                    /****************** End Parse Response JSON Data *************/
                    //Show Parsed Output on screen (activity)
                    jsonParsed.setText( OutputData );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
