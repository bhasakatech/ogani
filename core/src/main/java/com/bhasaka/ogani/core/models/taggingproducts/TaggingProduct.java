
package com.bhasaka.ogani.core.models.taggingproducts;
import java.util.Collections;
import java.util.List;

public class TaggingProduct {

    private final String title;
    private final String price;
    private final String image;
    private final List<String> categories;

    public TaggingProduct(String title, String price, String image, List<String> categories) {
        this.title = title != null ? title : "";
        this.price = price != null ? price : "";
        this.image = image != null ? image : "";
        this.categories = categories != null ? categories : Collections.emptyList();
    }

    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public List<String> getCategories() { return categories; }
}
