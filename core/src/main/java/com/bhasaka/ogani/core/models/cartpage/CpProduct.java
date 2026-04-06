package com.bhasaka.ogani.core.models.cartpage;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CpProduct {

    @ValueMapValue
    private String title;
    @ValueMapValue
    private String category;
    @ValueMapValue
    private String image;
    @ValueMapValue
    private double originalPrice;
    @ValueMapValue
    private double currentPrice;
    @ValueMapValue
    private double discount;

    // Getters & Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public double getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }
}