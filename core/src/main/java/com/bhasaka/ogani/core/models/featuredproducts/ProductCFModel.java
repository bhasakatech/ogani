package com.bhasaka.ogani.core.models.featuredproducts;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public String[] getCategory() { return category; }
}