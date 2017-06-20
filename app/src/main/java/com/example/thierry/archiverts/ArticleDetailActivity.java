package com.example.thierry.archiverts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;
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

        TextView txtProg = (TextView) findViewById(R.id.program);
        TextView txtTitle = (TextView) findViewById(R.id.title);
        TextView txtSummary = (TextView) findViewById(R.id.summary);
        TextView txtDate = (TextView) findViewById(R.id.publicationDate);
        TextView txtMediaType = (TextView) findViewById(R.id.mediaType);
        TextView txtDuration = (TextView) findViewById(R.id.duration);

        ImageView imgArticle = (ImageView) findViewById(R.id.imageArticle);

        txtProg.setText(a.getProgram());
        txtTitle.setText(a.getTitle());
        txtSummary.setText(a.getSummary());
        txtDate.setText(a.getPublicationDate().toString());
        txtMediaType.setText(a.getMediaType());
        txtDuration.setText(a.getDuration());

        // Display picture
        /*
        URL url = new URL("http://www.rts.ch/8623313.image");
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        imgArticle.setImageBitmap(bmp);*/

        // onClick program -> load a new search
        txtProg.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0) {
                Log.i("click","program");
            }
        });
    }
}
