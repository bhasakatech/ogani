package com.bhasaka.ogani.core.models.beans;

/**
 * POJO class representing a Category.
 * <p>
 * Holds category details such as tag identifier and display name.
 */
public class Category {

    private String tag;
    private String name;

    /**
     * Constructor to initialize category.
     *
     * @param tag   category tag identifier
     * @param name  category display name
     */
    public Category(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    /**
     * Returns category tag.
     *
     * @return tag identifier
     */
    public String getTag() { return tag; }

    /**
     * Returns category name.
     *
     * @return display name
     */
    public String getName() { return name; }
}