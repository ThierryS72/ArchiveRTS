package com.example.thierry.archiverts;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by wengerol on 16.06.2017.
 */

public class Article implements Serializable  {
    private String title;
    private String program;
    private String summary;
    private Date publicationDate;
    private URI imageURL;
    private URI articleURL;
    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");

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

    public String getPublicationDate() { return dt1.format(this.publicationDate); }
}
