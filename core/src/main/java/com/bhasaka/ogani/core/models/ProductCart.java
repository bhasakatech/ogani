package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model representing a product item in the cart.
 *
 * This class acts as a data holder for cart-specific product information.
 *
 * Properties are injected from JCR using ValueMapValue annotation.
 *
 * Fields include:
 * - title
 * - category
 * - image
 * - pricing information
 *
 * This model is typically populated manually in ProductCartModel.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCart {

    /**
     * Product title.
     */
    @ValueMapValue
    private String title;

    /**
     * Product category.
     */
    @ValueMapValue
    private String category;

    /**
     * Product image path.
     */
    @ValueMapValue
    private String image;

    /**
     * Original price before discount.
     */
    @ValueMapValue
    private double originalPrice;

    /**
     * Current price after discount.
     */
    @ValueMapValue
    private double currentPrice;

    /**
     * Discount percentage.
     */
    @ValueMapValue
    private double discount;

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

    /**
     * Sets discount value.
     *
     * @param discount discount percentage
     */
    public void setDiscount(int discount) { this.discount = discount; }
}