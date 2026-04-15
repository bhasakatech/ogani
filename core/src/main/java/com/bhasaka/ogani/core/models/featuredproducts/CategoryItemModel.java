package com.bhasaka.ogani.core.models.featuredproducts;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model class representing a category item.
 * <p>
 * This class is used to fetch category tag values from the resource.
 */
@Model(adaptables = org.apache.sling.api.resource.Resource.class)
public class CategoryItemModel {

    @ValueMapValue
    private String categoryTag;

    /**
     * Returns category tag value.
     *
     * @return category tag
     */
    public String getCategoryTag() {
        return categoryTag;
    }
}