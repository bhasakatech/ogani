package com.bhasaka.ogani.core.models.taggingproducts;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TaggingCfModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String price;

    @ValueMapValue
    private String image;

    @ValueMapValue(name = "cq:tags")
    private String[] categories;


    public String getTitle() {
        return title != null ? title : "";
    }

    public String getPrice() {
        return price != null ? price : "";
    }

    public String getImage() {
        return image != null ? image : "";
    }

    public String[] getCategories() {
        return categories != null ? categories : new String[0];
    }
}