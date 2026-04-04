package com.bhasaka.ogani.core.models.beans;

public class Category {
    private String tag;
    private String name;

    public Category(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() { return tag; }
    public String getName() { return name; }
}
