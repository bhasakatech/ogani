
 package com.bhasaka.ogani.core.models.taggingproducts;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * POJO representing a product with tagging information.
 *
 * <p>This class stores product details such as title, price,
 * image, and associated category tags.</p>
 *
 * <p>Null-safe initialization is applied to ensure no null values
 * are exposed to consumers.</p>
 */
public class TaggingProduct {

    /**
     * Logger for tracking object creation.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TaggingProduct.class);

    /**
     * Product title.
     */
    private final String title;

    /**
     * Product price.
     */
    private final String price;

    /**
     * Product image path.
     */
    private final String image;

    /**
     * List of category tags.
     */
    private final List<String> categories;

    /**
     * Constructs a TaggingProduct instance.
     *
     * <p>Applies null-safety to all fields.</p>
     *
     * @param title product title
     * @param price product price
     * @param image product image path
     * @param categories list of category tags
     */
    public TaggingProduct(String title, String price, String image, List<String> categories) {
        this.title = title != null ? title : "";
        this.price = price != null ? price : "";
        this.image = image != null ? image : "";
        this.categories = categories != null ? categories : Collections.emptyList();

        // Minimal useful debug log (object creation summary)
        LOG.debug("TaggingProduct created with title='{}' and {} categories",
                this.title, this.categories.size());
    }

    /**
     * Returns product title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns product price.
     *
     * @return price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Returns product image path.
     *
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns category tags.
     *
     * @return list of categories
     */
    public List<String> getCategories() {
        return categories;
    }
}