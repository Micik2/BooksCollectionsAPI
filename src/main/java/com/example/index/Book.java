package com.example.index;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.lang.*;


public class Book {
    private String id;
    private String isbn;
    private String title;
    private String subtitle;
    private String publisher;
    private long publishedDate;
    private String description;
    private Integer pageCount;
    private String thumbnailUrl;
    private String language;
    private String previewLink;
    private Double averageRating;
    private ArrayList<String> authors;
    private ArrayList<String> categories;

    public Book() {}

    public Book(String i, String is, String t, String s, String p, String pD, String d, Integer pC, String tU, String l, String pL, Double aR, ArrayList<String> a, ArrayList<String> c) {
        this.id = i;
        this.isbn = is;
        this.title = t;
        this.subtitle = s;
        this.publisher = p;
        if (pD.equals(""))
            this.publishedDate = 0;
        else
            this.publishedDate = getFormat(pD);
        this.description = d;
        this.pageCount = pC;
        this.thumbnailUrl = tU;
        this.language = l;
        this.previewLink = pL;
        this.averageRating = aR;
        this.authors = a;
        this.categories = c;
    }
    

    public long getFormat(String date) {
        SimpleDateFormat simpleDateFormat = null;
        Date d = null;
        if (date.indexOf('-') == -1)
            simpleDateFormat = new SimpleDateFormat("yyyy");
        else if (date.indexOf('-') > 0)
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        else
            return 0;
        try {
            d = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long miliseconds = d.getTime();
        return miliseconds;
    }

    public String getId() {
        return this.id;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public long getPublishedDate() {
        return this.publishedDate;
    }

    public String getDescription() {return this.description; }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getPreviewLink() {
        return this.previewLink;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public ArrayList<String> getAuthors() {
        return this.authors;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }
}