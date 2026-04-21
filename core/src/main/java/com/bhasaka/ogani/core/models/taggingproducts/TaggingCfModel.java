package com.bhasaka.ogani.core.models.taggingproducts;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model representing a single tagged Content Fragment product.
 *
 * <p>This model retrieves product-related fields such as:
 * title, price, image, and associated category tags.</p>
 *
 * <p>It ensures null-safe return values for all properties.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TaggingCfModel {

    /**
     * Logger for debugging field values.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TaggingCfModel.class);

    /**
     * Product title.
     */
    @ValueMapValue
    private String title;

    /**
     * Product price.
     */
    @ValueMapValue
    private String price;

    /**
     * Product image path.
     */
    @ValueMapValue
    private String image;

    /**
     * Category tags associated with the product.
     */
    @ValueMapValue(name = "cq:tags")
    private String[] categories;

    /**
     * Returns product title.
     *
     * @return title if available, otherwise empty string
     */
    public String getTitle() {
        return title != null ? title : "";
    }

    /**
     * Returns product price.
     *
     * @return price if available, otherwise empty string
     */
    public String getPrice() {
        return price != null ? price : "";
    }

    /**
     * Returns product image path.
     *
     * @return image if available, otherwise empty string
     */
    public String getImage() {
        return image != null ? image : "";
    }

    /**
     * Returns category tags.
     *
     * @return array of category tags, or empty array if null
     */
    public String[] getCategories() {
        return categories != null ? categories : new String[0];
    }
}