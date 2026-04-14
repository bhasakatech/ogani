package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sling Model for Product Cart component.
 *
 * This model is responsible for reading product references from the component,
 * fetching product details from JCR, and preparing data for cart display.
 *
 * Data Source:
 * - Reads child resources under "products"
 * - Each child contains a reference path (cfPath)
 * - Fetches product data from content fragment structure
 *
 * Responsibilities:
 * - Iterate through child product references
 * - Fetch product data using ResourceResolver
 * - Map data into ProductCart objects
 * - Provide labels and links for cart UI
 *
 * Behavior:
 * - Skips invalid or missing product references
 * - Handles null values safely
 * - Returns empty list if no valid data found
 *
 * Injection Strategy:
 * Uses OPTIONAL injection, so missing properties do not cause failure.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCartModel {

    /**
     * List of product reference resources configured in the component.
     */
    @ChildResource(name = "products")
    private List<Resource> products;

    /**
     * ResourceResolver used to fetch product data from JCR.
     */
    @SlingObject
    private ResourceResolver resolver;

    /**
     * List of processed cart products.
     */
    private List<ProductCart> productList = new ArrayList<>();

    // Labels and UI properties

    @ValueMapValue private String productsLabel;
    @ValueMapValue private String priceLabel;
    @ValueMapValue private String quantityLabel;
    @ValueMapValue private String totalLabel;

    @ValueMapValue private String couponPlaceholder;
    @ValueMapValue private String applyCouponLabel;

    @ValueMapValue private String cartTotalLabel;
    @ValueMapValue private String subtotalLabel;
    @ValueMapValue private String totalSummaryLabel;

    @ValueMapValue private String checkoutLabel;
    @ValueMapValue private String continueShoppingLabel;
    @ValueMapValue private String updateCartLabel;

    @ValueMapValue private String continueShoppingLink;
    @ValueMapValue private String updateCartLink;
    @ValueMapValue private String checkoutLink;

    /**
     * Initializes the cart model after dependency injection.
     *
     * Flow:
     * - Iterate through product references
     * - Read cfPath from each item
     * - Fetch data from JCR path
     * - Map properties to ProductCart object
     *
     * Skips:
     * - Null or empty paths
     * - Missing resources
     * - Any exception during processing
     */
    @PostConstruct
    protected void init() {

        if (products == null || products.isEmpty()) {
            return;
        }

        for (Resource item : products) {

            try {
                String cfPath = item.getValueMap().get("cfPath", String.class);

                if (cfPath == null || cfPath.isEmpty()) {
                    continue;
                }

                Resource dataResource = resolver.getResource(cfPath + "/jcr:content/data/master");

                if (dataResource == null) {
                    continue;
                }

                ValueMap valueMap = dataResource.getValueMap();

                ProductCart product = new ProductCart();

                product.setTitle(valueMap.get("title", ""));
                product.setCategory(valueMap.get("category", ""));

                String imageValue = valueMap.get("image", String.class);
                product.setImage(resolveImage(imageValue));

                double currentPrice = valueMap.get("currentPrice", 0.0);
                product.setCurrentPrice(currentPrice);

                productList.add(product);

            } catch (Exception ignored) {
                // intentionally ignored to prevent UI break
            }
        }
    }

    /**
     * Resolves image path.
     *
     * Logic:
     * - Returns empty string if null or empty
     * - Returns DAM path as is
     * - Resolves external path using resolver
     *
     * @param value image path
     * @return resolved image path
     */
    private String resolveImage(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        if (value.startsWith("/content/dam")) {
            return value;
        }

        Resource resolved = resolver.resolve(value);
        return resolved != null ? resolved.getPath() : value;
    }

    /**
     * Returns list of cart products.
     *
     * @return list of ProductCart
     */
    public List<ProductCart> getProductList() {
        return productList;
    }

    /**
     * Checks whether cart is empty.
     *
     * @return true if no products available
     */
    public boolean isEmpty() {
        return productList == null || productList.isEmpty();
    }

    public String getProductsLabel() { return productsLabel; }
    public String getPriceLabel() { return priceLabel; }
    public String getQuantityLabel() { return quantityLabel; }
    public String getTotalLabel() { return totalLabel; }

    public String getCouponPlaceholder() { return couponPlaceholder; }
    public String getApplyCouponLabel() { return applyCouponLabel; }

    public String getCartTotalLabel() { return cartTotalLabel; }
    public String getSubtotalLabel() { return subtotalLabel; }
    public String getTotalSummaryLabel() { return totalSummaryLabel; }

    public String getCheckoutLabel() { return checkoutLabel; }
    public String getContinueShoppingLabel() { return continueShoppingLabel; }
    public String getUpdateCartLabel() { return updateCartLabel; }

    public String getContinueShoppingLink() { return continueShoppingLink; }
    public String getUpdateCartLink() { return updateCartLink; }
    public String getCheckoutLink() { return checkoutLink; }
}