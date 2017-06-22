package com.example.thierry.archiverts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by WengerOl on 17.06.2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    //articles est la liste des models à afficher
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_list,parent, false);
        }

        ArticleViewHolder viewHolder = (ArticleViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ArticleViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
            viewHolder.program = (TextView) convertView.findViewById(R.id.program);
            viewHolder.date = (TextView) convertView.findViewById(R.id.publicationDate);
            viewHolder.imageArticle = (ImageView) convertView.findViewById(R.id.imageArticle);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Article> articles
        Article article = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText(article.getTitle());
        viewHolder.summary.setText(article.getSummary());
        viewHolder.program.setText(article.getProgram());
        //viewHolder.imageArticle.setImageURI(Uri.parse(article.getImageURL()));

        if (article.getImageURL() != "") {
            viewHolder.imageArticle.setVisibility(View.VISIBLE);
            new ImageDownloaderTask(viewHolder.imageArticle).execute(article.getImageURL());
        }
        else  // If there isn't a picture, hide the empty space with GONE
        {
            viewHolder.imageArticle.setVisibility(View.GONE);
        }
        /*
        Uri newUri  = Uri.parse((article.getPicture().toString()));
        viewHolder.imageArticle.setImageURI(newUri);
        */
        viewHolder.date.setText(article.getPublicationDate());
        //viewHolder.avatar.setImageDrawable(new ColorDrawable(article.getColor()));

        // background color for even / unpair
        if (position == 0) {
            //convertView.setBackgroundResource()
        } else if (position == 1) {
            //convertView.setBackgroundResource(ColorDrawable-)
        }

        return convertView;
    }

    private class ArticleViewHolder{
        public TextView title;
        public TextView summary;
        public TextView program;
        public TextView date;
        public ImageView imageArticle;
    }
}