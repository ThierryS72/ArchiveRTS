package com.example.thierry.archiverts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private List<Article> articles = loadArticle();
    ArticleAdapter adapter;
    private String searchStringQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // ListView
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new ArticleAdapter(MainActivity.this, articles);
        mListView.setAdapter(adapter);


        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        final Button GetServerData = (Button) findViewById(R.id.GetServerData);

        GetServerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            // WebServer Request URL
            //String serverURL = "http://androidexample.com/media/webservice/JsonReturn.php";
            EditText searchString = (EditText) findViewById(R.id.searchString);
            searchStringQuery = searchString.getText().toString();
            String apiKey = getString(R.string.apikey);
            String serverURL = "http://srgssr-prod.apigee.net/rts-archives-public-api/archives?apikey="+apiKey+"&query="+searchStringQuery+"&enumeratedFacets=mediaType&rows=10";
            Log.i("Search URL : ",serverURL);
            Log.i("Search keyword : ",searchStringQuery);
            // Use AsyncTask execute Method To Prevent ANR Problem
            new LongOperation().execute(serverURL);
        }});

        // Article detail activity when click on an item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("ListView item","click" + position);
                Intent myIntent = new Intent(view.getContext(), ArticleDetailActivity.class);
                myIntent.putExtra("article", articles.get(position));
                startActivityForResult(myIntent, position);
            }
        });
    }

    private List<Article> loadArticle()
    {
        List<Article> articles = new ArrayList<Article>();
        /*
        articles.add(new Article("test", "program test", "Résumé test", new Date()));
        articles.add(new Article("test 2", "program test", "Résumé test", new Date()));
        articles.add(new Article("test 3", "program test", "Résumé test", new Date()));
        */
        return articles;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data ="";
        TextView uiUpdate = (TextView) findViewById(R.id.output);
        TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;
        EditText searchString = (EditText) findViewById(R.id.searchString);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
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

            } else {

                // Show Response Json On Screen (activity)
                uiUpdate.setText( Content );

                // Reset listArticles
                articles = loadArticle();

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
                    String excerpt;
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
                        date = new Date();
                        try{
                            publicationDate = (String) (docs.getJSONObject(i).get("publicationDate"));
                            date = formatter.parse(publicationDate.replaceAll("Z$", "+0000"));
                        }
                        catch(Exception e){
                            publicationDate = "";
                        }
                        try{
                            excerpt = (String) (docs.getJSONObject(i).get("excerpt"));
                        }
                        catch(Exception e){
                            excerpt = "";
                        }
                        Log.i(i +":", "titre d'émission:" + program + " titre: " + title + " Date de publication: " + publicationDate + "Résumé: " + excerpt );

                        OutputData += " program          : "+ program;

                        // add article to articleList
                        // if(excerpt.length() > 100) excerpt = excerpt.substring(0,100) + "...";
                        articles.add(new Article(title,program,excerpt,date));
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
