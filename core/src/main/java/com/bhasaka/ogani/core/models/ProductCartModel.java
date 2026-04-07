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

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCartModel {

    @ChildResource(name = "products")
    private List<Resource> products;

    @SlingObject
    private ResourceResolver resolver;

    private List<ProductCart> productList = new ArrayList<>();

    @ValueMapValue
    private String productsLabel;

    @ValueMapValue
    private String priceLabel;

    @ValueMapValue
    private String quantityLabel;

    @ValueMapValue
    private String totalLabel;

    @ValueMapValue
    private String couponPlaceholder;

    @ValueMapValue
    private String applyCouponLabel;

    @ValueMapValue
    private String cartTotalLabel;

    @ValueMapValue
    private String subtotalLabel;

    @ValueMapValue
    private String totalSummaryLabel;

    @ValueMapValue
    private String checkoutLabel;

    @ValueMapValue
    private String continueShoppingLabel;

    @ValueMapValue
    private String updateCartLabel;

    @ValueMapValue
    private String continueShoppingLink;

    @ValueMapValue
    private String updateCartLink;

    @ValueMapValue
    private String checkoutLink;

    public String getContinueShoppingLink() {
        return continueShoppingLink;
    }

    public String getUpdateCartLink() {
        return updateCartLink;
    }

    public String getCheckoutLink() {
        return checkoutLink;
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

            } catch (Exception ignored) {}
        }
    }
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

    public List<ProductCart> getProductList() {
        return productList;
    }

    public boolean isEmpty() {
        return productList == null || productList.isEmpty();
    }
}