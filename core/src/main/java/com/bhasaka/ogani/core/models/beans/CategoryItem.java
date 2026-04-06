package com.bhasaka.ogani.core.models.beans;

public class CategoryItem {

    private String tagId;
    private String title;
    private int count;

    public CategoryItem(String tagId, String title, int count) {
        this.tagId = tagId;
        this.title = title;
        this.count = count;
    }

    public String getTagId() { return tagId; }
    public String getTitle() { return title; }
    public int getCount() { return count; }
}
