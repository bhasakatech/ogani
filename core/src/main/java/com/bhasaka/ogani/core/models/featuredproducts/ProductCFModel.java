package com.bhasaka.ogani.core.models.featuredproducts;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Model class representing Product Content Fragment data.
 * <p>
 * This class maps product properties such as title, price,
 * image, and category from the resource.
 */
@Model(adaptables = org.apache.sling.api.resource.Resource.class)
public class ProductCFModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String price;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String[] category;

    /**
     * Returns product title.
     *
     * @return title of the product
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns product price.
     *
     * @return price of the product
     */
    public String getPrice() {
        return price;
    }

    /**
     * Returns product image path.
     *
     * @return image reference
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns product categories.
     *
     * @return array of category tags
     */
    public String[] getCategory() {
        return category;
    }
}