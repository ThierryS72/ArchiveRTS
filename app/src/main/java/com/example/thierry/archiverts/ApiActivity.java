package com.example.thierry.archiverts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by WengerOl on 19.06.2017.
 */

public class ApiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        // Get the article
        Intent i = getIntent();
        String apiKey = getString(R.string.apikey);
        String apiServerUrl = getString(R.string.api_url);
        String searchString = (String)i.getStringExtra("searchString");
        String sort = (String)i.getStringExtra("sort");
        String enumFacets = (String)i.getStringExtra("enumFacets");
        int nbRows = (int)i.getIntExtra("nbRows", 0);

        try {
            OkHttpClient httpClient = new OkHttpClient();
            String serverURL = apiServerUrl+"?apikey="+apiKey+"&query="+searchString+"&enumeratedFacets="+enumFacets+"&rows="+nbRows+"&sort="+sort;
            Request getRequestApi = new Request.Builder()
                    .url(serverURL)
                    .build();

            httpClient.newCall(getRequestApi).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful())
                    {
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        finish();
                    }
                    String res = response.body().string();
                    Log.i("okHttp",res);
                    // An intent is used to return values (response string from API)
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("response", res);
                    setResult(Activity.RESULT_OK, resultIntent);
                    // finish the activity
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            Log.i("okhttp exception",e.getMessage());
        }
    }
}
