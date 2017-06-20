package com.example.thierry.archiverts;

import android.net.Uri;

import java.net.URI;
import java.net.URL;
import java.util.Date;

/**
 * Created by wengerol on 16.06.2017.
 */

public class Article {
    private String title;
    private String program;
    private String summary;
    private Date publicationDate;
    private URI imageURL;
    private URI articleURL;

    public Article(String title, String program, String summary, Date publicationDate)
    {
        this.title = title;
        this.program = program;
        this.summary = summary;
        this.publicationDate = publicationDate;
    }

    public Article(String title, String program, String summary, Date publicationDate, URI imageURL, URI articleURL)
    {
        this.title = title;
        this.program = program;
        this.summary = summary;
        this.publicationDate = publicationDate;
        this.imageURL = imageURL;
        this.articleURL = articleURL;
    }

    public String getTitle() {
        return this.title;
    }

    public String getProgram() {
        return this.program;
    }

    public String getSummary() {
        return this.summary;
    }

    public URI getPicture() { return this.imageURL; }
}
