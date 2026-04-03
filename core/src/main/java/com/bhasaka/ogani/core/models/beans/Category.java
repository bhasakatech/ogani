package com.bhasaka.ogani.core.models.beans;
public class Category {

    private String title;
    private String image;
    private String link;

    public Category(String title, String image, String link) {
        this.title = title;
        this.image = image;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }
}
