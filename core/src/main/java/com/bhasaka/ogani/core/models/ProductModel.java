
package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductModel {

    @ValueMapValue
    private String text;

    @ValueMapValue
    private String fragmentFolder;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<Product> products = new ArrayList<>();

    public String getText() {
        return text;
    }

    public List<Product> getProducts() {
        return products;
    }
   
public List<List<Product>> getSlides() {

    List<List<Product>> slides = new ArrayList<>();
    int itemsPerSlide = 3;

    for (int i = 0; i < products.size(); i += itemsPerSlide) {

        int end = Math.min(i + itemsPerSlide, products.size());

        slides.add(products.subList(i, end));
    }

    return slides;
}

    @PostConstruct
    protected void init() {

        if (fragmentFolder == null || fragmentFolder.isEmpty()) {
            return;
        }

        Resource folderResource = resourceResolver.getResource(fragmentFolder);

        if (folderResource == null) {
            return;
        }

        for (Resource child : folderResource.getChildren()) {

            if ("jcr:content".equals(child.getName())) {
                continue;
            }

            Resource dataNode = child.getChild("jcr:content/data/master");

            if (dataNode != null) {

                String image = dataNode.getValueMap().get("image", String.class);
                String title = dataNode.getValueMap().get("title", String.class);
                String price = dataNode.getValueMap().get("price", String.class);

                products.add(new Product(image, title, price));
            }
        }
    }

    public static class Product {

        private String image;
        private String title;
        private String price;

        public Product(String image, String title, String price) {
            this.image = image;
            this.title = title;
            this.price = price;
        }

        public String getImage() { return image; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
    }
}

