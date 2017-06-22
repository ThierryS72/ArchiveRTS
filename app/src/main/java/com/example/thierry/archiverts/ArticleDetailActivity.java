package com.example.thierry.archiverts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by WengerOl on 19.06.2017.
 */

public class ArticleDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        // Get the article
        Intent i = getIntent();
        Article a = (Article)i.getSerializableExtra("article");
        final String keyword = (String)i.getStringExtra("keyword");

        final TextView txtProg = (TextView) findViewById(R.id.program);
        TextView txtTitle = (TextView) findViewById(R.id.title);
        TextView txtSummary = (TextView) findViewById(R.id.summary);
        TextView txtDate = (TextView) findViewById(R.id.publicationDate);
        TextView txtMediaType = (TextView) findViewById(R.id.mediaType);
        TextView txtDuration = (TextView) findViewById(R.id.duration);
        TextView txtArticleUrl = (TextView) findViewById(R.id.articleUrl);

        final String articleUrl = a.getArticleURL();
        final String imageUrl = a.getImageUrl();

        ImageView imgArticle = (ImageView) findViewById(R.id.imageArticle);

        txtProg.setText(a.getProgram());
        txtTitle.setText(a.getTitle());
        txtSummary.setText(a.getSummary());
        txtDate.setText(a.getPublicationDate());
        txtMediaType.setText(a.getMediaType());
        txtDuration.setText(a.getDuration());
        txtArticleUrl.setText(articleUrl);

        // Display picture
        new DownloadImageFromInternet((ImageView) findViewById(R.id.imageArticle))
                .execute(a.getImageUrl());
        Log.i("Image",a.getImageUrl());

        // If there isn't a picture, hide the empty space with GONE
        if(imageUrl != "")
        {
            imgArticle.setVisibility(View.VISIBLE);
        }
        else
        {
            imgArticle.setVisibility(View.GONE);
        }

        // onClick program -> load a new search
        txtProg.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0) {

                Log.i("click","program");
                // An intent is used to return values (response string from API)
                Intent resultIntent = new Intent();
                resultIntent.putExtra("program", txtProg.getText());
                resultIntent.putExtra("keyword", keyword);
                setResult(Activity.RESULT_OK, resultIntent);
                // finish the activity
                finish();
            }
        });

        imgArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Article Url Img",articleUrl);
                if(articleUrl != "") {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                    startActivity(browserIntent);
                }
            }
        });

        txtArticleUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Article Url link",articleUrl);
                if(articleUrl != "") {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                    startActivity(browserIntent);
                }
            }
        });
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            // Toast.makeText(getApplicationContext(), "Chargement image...", Toast.LENGTH_SHORT).show();
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
}
