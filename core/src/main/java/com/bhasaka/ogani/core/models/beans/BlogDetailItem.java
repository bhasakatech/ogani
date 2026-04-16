package com.bhasaka.ogani.core.models.beans;

public class BlogDetailItem {
    private String title;
    private String path;
    private String image;
    private String publishDate;

    public BlogDetailItem(String title, String path, String image, String publishDate) {
        this.title = title;
        this.path = path;
        this.image = image;
        this.publishDate = publishDate;
    }

    public String getTitle() { return title; }
    public String getPath() { return path; }
    public String getImage() { return image; }
    public String getPublishDate() { return publishDate; }
}
