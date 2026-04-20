package com.bhasaka.ogani.core.models.beans;

/**
 * POJO class representing a Blog Detail item.
 * <p>
 * Contains blog metadata such as title, path,
 * image reference, and publish date.
 */
public class BlogDetailItem {

    private String title;
    private String path;
    private String image;
    private String publishDate;

    /**
     * Constructor to initialize blog detail item.
     *
     * @param title        blog title
     * @param path         blog page path
     * @param image        blog image path
     * @param publishDate  formatted publish date
     */
    public BlogDetailItem(String title, String path, String image, String publishDate) {
        this.title = title;
        this.path = path;
        this.image = image;
        this.publishDate = publishDate;
    }

    /**
     * Returns blog title.
     *
     * @return title of the blog
     */
    public String getTitle() { return title; }

    /**
     * Returns blog page path.
     *
     * @return blog path
     */
    public String getPath() { return path; }

    /**
     * Returns blog image.
     *
     * @return image path
     */
    public String getImage() { return image; }

    /**
     * Returns publish date.
     *
     * @return formatted publish date
     */
    public String getPublishDate() { return publishDate; }
}