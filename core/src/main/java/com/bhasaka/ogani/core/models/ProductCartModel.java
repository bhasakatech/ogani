package com.bhasaka.ogani.core.models;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = org.apache.sling.api.resource.Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCartModel {

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