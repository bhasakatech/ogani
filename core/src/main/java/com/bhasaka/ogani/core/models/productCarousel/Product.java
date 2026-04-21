package com.bhasaka.ogani.core.models.productCarousel;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model representing a Product entity used in the Product Carousel component.
 *
 * This model adapts from a Sling Resource and maps product-related
 * properties from the JCR repository using ValueMapValue injection.
 *
 * It acts as a simple POJO (Data Transfer Object) to hold product data such as
 * title, category, image path, pricing, and discount information.
 *
 * Usage:
 * - Used by service layer to populate product data
 * - Can also be adapted directly from a resource if required
 * - Consumed by HTL for rendering product details
 *
 * Injection Strategy:
 * Uses DefaultInjectionStrategy.OPTIONAL, meaning missing properties
 * will not throw errors and default values will be used.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Product {

    /**
     * Title of the product.
     */
    @ValueMapValue
    private String title;

    /**
     * Category to which the product belongs.
     */
    @ValueMapValue
    private String category;

    /**
     * Path of the product image (DAM or external).
     */
    @ValueMapValue
    private String image;

    /**
     * Original price of the product before discount.
     */
    @ValueMapValue
    private double originalPrice;

    /**
     * Current price of the product after discount.
     */
    @ValueMapValue
    private double currentPrice;

    private double price = getCurrentPrice();

    /**
     * Discount percentage applied to the product.
     */
    @ValueMapValue
    private double discount;

    /**
     * Returns the product title.
     *
     * @return product title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the product title.
     *
     * @param title product title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the product category.
     *
     * @return product category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     *
     * @param category product category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the product image path.
     *
     * @return image path
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the product image path.
     *
     * @param image image path
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Returns the original price of the product.
     *
     * @return original price
     */
    public double getOriginalPrice() {
        return originalPrice;
    }

    /**
     * Sets the original price of the product.
     *
     * @param originalPrice original price
     */
    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * Returns the current price of the product.
     *
     * @return current price
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Sets the current price of the product.
     *
     * @param currentPrice current price
     */
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * Returns the discount percentage.
     *
     * @return discount percentage
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Sets the discount percentage.
     *
     * @param discount discount percentage
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }
}