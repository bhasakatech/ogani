package com.bhasaka.ogani.core.models.bloglisting;

import java.util.Date;

public class BlogItem {

    private String title;
    private String description;
    private String image;
    private String path;
    private String publishDate;
    private Date date;

    public BlogItem(String title, String description, String image, String path, String publishDate, Date date) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.path = path;
        this.publishDate = publishDate;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public String getPath() { return path; }
    public String getPublishDate() { return publishDate; }
    public Date getDate() { return date; }
}