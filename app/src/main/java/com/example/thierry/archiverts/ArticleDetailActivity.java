package com.example.thierry.archiverts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by WengerOl on 19.06.2017.
 */

public class ArticleDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        TextView txtProg = (TextView) findViewById(R.id.program);
        txtProg.setText("TEST Prog");
    }
}
