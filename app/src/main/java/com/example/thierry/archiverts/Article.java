package com.example.thierry.archiverts;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import android.graphics.drawable.Drawable;

/**
 * Created by wengerol on 16.06.2017.
 */

public class Article implements Serializable  {
    private String title;
    private String program;
    private String summary;
    private Date publicationDate;
    private int duration;
    private String imageURL;
    private URL imageURL2;
    private String articleURL;
    public enum mediaTypeEnum {
        audio,
        video,
        article
    }
    private mediaTypeEnum mediaType;

    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");

    public Article(String title, String program, String summary, Date publicationDate, String mediaType, int duration, String imageURL, String articleURL)
    {
        this.title = title;
        this.program = program;
        this.summary = summary;
        this.publicationDate = publicationDate;
        this.mediaType = mediaTypeEnum.valueOf(mediaType);
        this.duration = duration;
        this.imageURL = imageURL;
        this.articleURL = articleURL;

    }

    private Drawable LoadImageFromWebOperations() {
        if (this.imageURL != "") {
            try {
                imageURL2 = new URL(this.imageURL);
                InputStream is = (InputStream) this.imageURL2.getContent();
                Drawable d = Drawable.createFromStream(is, "RTS");
                return d;
            } catch (Exception e) {
                return null;
            }
        }
        else
        { return null; }
    }

    // getters
    public String getTitle() {
        return this.title;
    }
    public String getProgram() {
        return this.program;
    }
    public String getSummary() {
        return this.summary;
    }
    public String getImageUrl() {
        return this.imageURL;
    }
    public String getPublicationDate() { return dt1.format(this.publicationDate); }
    public String getMediaType() { return this.mediaType.name(); }

    public String getDuration() {
        if(this.duration > 0)
        {
            int h = 0;
            int m = 0;
            int s = 0;
            int solde = this.duration;
            String res = "";

            if (solde > 3600)
            {
                h = this.duration / 3600;
                solde %= 3600;
                res = h+"h";
            }
            if (solde > 60)
            {
                m = solde / 60;
                solde %= 60;
                if (m < 10)
                {
                    res += "0" + m + "m";
                }
                else
                {
                    res += m + "m";
                }
            }
            if (solde > 0)
            {
                s = solde;
                if (s < 10)
                {
                    res += "0" + s + "s";
                }
                else
                {
                    res += s + "s";
                }
            }
            return res;
        }
        else {
            return "";
        }
    }
    public String getImageURL() { return this.imageURL; }
    public String getArticleURL() { return  this.articleURL; }
}
