package com.example.thierry.archiverts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private List<Article> articles = resetArticles();
    ArticleAdapter adapter;

    private String searchStringQuery = "";
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mContext = this;
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
        final Button BtnGetServerData = (Button) findViewById(R.id.GetServerData);
        final Context c = this.getApplicationContext();

        BtnGetServerData.setOnClickListener(new View.OnClickListener() {
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
            String apiServerUrl = getString(R.string.api_url);
            String serverURL = apiServerUrl+"?apikey="+apiKey+"&query="+searchStringQuery+"&enumeratedFacets=mediaType&rows=100&sort=-score%2C-isDuplicate%2C-publicationDate";
            Log.i("Search URL : ",serverURL);
            Log.i("Search keyword : ",searchStringQuery);
            // Intent for API activity
            Intent myIntent = new Intent(mListView.getContext(), ApiActivity.class);
            myIntent.putExtra("searchString", searchStringQuery);
            myIntent.putExtra("sort", "-score%2C-isDuplicate%2C-publicationDate");
            myIntent.putExtra("enumFacets", "mediaType,program");
            myIntent.putExtra("nbRows", 25);
            myIntent.putExtra("start", 0);
            try {
                startActivityForResult(myIntent, 200); // 200 is used for responseCode
            }catch (Exception e)
            {
                e.getMessage();
            }
        }});

        // Article detail activity when click on an item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("ListView item","click" + position);
                Intent myIntent = new Intent(view.getContext(), ArticleDetailActivity.class);
                myIntent.putExtra("article", articles.get(position));
                myIntent.putExtra("keyword", searchStringQuery);
                startActivityForResult(myIntent, 100); // 100 used for responseCode
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 100 means activity articleDetail
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String keyword = data.getStringExtra("keyword");
            String program = data.getStringExtra("program");
            // Start a new search for this emission
            Intent myIntent = new Intent(mListView.getContext(), ApiActivity.class);
            myIntent.putExtra("searchString", "program:"+program+" AND "+keyword);
            myIntent.putExtra("sort", "-score%2C-isDuplicate%2C-publicationDate");
            myIntent.putExtra("enumFacets", "mediaType,program");
            myIntent.putExtra("nbRows", 25);
            myIntent.putExtra("start", 0);
            try {
                startActivityForResult(myIntent, 200); // 200 is used for responseCode
            }catch (Exception e)
            {
                e.getMessage();
            }
        }
        // 200 means activity articleDetail
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            String res = data.getStringExtra("response");
            Log.i("return intent",res);
            articles = Tools.responseApiJSONtoListArticles(res);

            mListView = (ListView) findViewById(R.id.listView);
            adapter = new ArticleAdapter(this, articles);
            mListView.setAdapter(adapter);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private List<Article> resetArticles()
    {
        List<Article> articles = new ArrayList<Article>();
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
}
