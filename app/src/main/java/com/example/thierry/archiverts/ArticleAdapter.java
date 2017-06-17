package com.example.thierry.archiverts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_detail,parent, false);
        }

        ArticleViewHolder viewHolder = (ArticleViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ArticleViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
            viewHolder.program = (TextView) convertView.findViewById(R.id.program);
            viewHolder.imageArticle = (ImageView) convertView.findViewById(R.id.imageArticle);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Article> articles
        Article article = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText(article.getTitle());
        viewHolder.summary.setText(article.getSummary());
        viewHolder.program.setText(article.getProgram());
        //viewHolder.avatar.setImageDrawable(new ColorDrawable(article.getColor()));

        return convertView;
    }

    private class ArticleViewHolder{
        public TextView title;
        public TextView summary;
        public TextView program;
        public ImageView imageArticle;
    }
}