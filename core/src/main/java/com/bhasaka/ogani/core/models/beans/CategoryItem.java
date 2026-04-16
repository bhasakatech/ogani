package com.bhasaka.ogani.core.models.beans;

/**
 * POJO class representing a Category Item.
 * <p>
 * Contains category details such as tag ID, title,
 * and associated blog count.
 */
public class CategoryItem {

    private String tagId;
    private String title;
    private int count;

    /**
     * Constructor to initialize category item.
     *
     * @param tagId  category tag identifier
     * @param title  category title
     * @param count  number of items under this category
     */
    public CategoryItem(String tagId, String title, int count) {
        this.tagId = tagId;
        this.title = title;
        this.count = count;
    }

    /**
     * Returns category tag ID.
     *
     * @return tag identifier
     */
    public String getTagId() { return tagId; }

    /**
     * Returns category title.
     *
     * @return category title
     */
    public String getTitle() { return title; }

    /**
     * Returns item count.
     *
     * @return number of items in this category
     */
    public int getCount() { return count; }
}