package com.bhasaka.ogani.core.models.featuredproducts;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = org.apache.sling.api.resource.Resource.class)
public class CategoryItemModel {

    @ValueMapValue
    private String categoryTag;

    public String getCategoryTag() {
        return categoryTag;
    }
}