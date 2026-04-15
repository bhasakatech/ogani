package com.bhasaka.ogani.core.models.beans;

import java.util.Date;

/**
 * POJO class representing a Blog item.
 * <p>
 * Contains blog details such as title, description,
 * image, path, publish date, and raw date for sorting.
 */
public class BlogItem {

    private String title;
    private String description;
    private String image;
    private String path;
    private String publishDate;
    private Date date;

    /**
     * Constructor to initialize blog item.
     *
     * @param title        blog title
     * @param description  blog description
     * @param image        blog image path
     * @param path         blog page path
     * @param publishDate  formatted publish date
     * @param date         raw date object (used for sorting)
     */
    public BlogItem(String title, String description, String image, String path, String publishDate, Date date) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.path = path;
        this.publishDate = publishDate;
        this.date = date;
    }

    /**
     * Returns blog title.
     *
     * @return title of the blog
     */
    public String getTitle() { return title; }

    /**
     * Returns blog description.
     *
     * @return blog description
     */
    public String getDescription() { return description; }

    /**
     * Returns blog image.
     *
     * @return image path
     */
    public String getImage() { return image; }

    /**
     * Returns blog page path.
     *
     * @return blog path
     */
    public String getPath() { return path; }

    /**
     * Returns formatted publish date.
     *
     * @return publish date
     */
    public String getPublishDate() { return publishDate; }

    /**
     * Returns raw date.
     *
     * @return date object
     */
    public Date getDate() { return date; }
}