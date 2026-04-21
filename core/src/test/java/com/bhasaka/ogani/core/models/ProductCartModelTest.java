package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductCartModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(ProductCartModel.class);

        context.create().resource("/content/test",
                "productsLabel", "Products",
                "priceLabel", "Price",
                "quantityLabel", "Quantity",
                "totalLabel", "Total",
                "couponPlaceholder", "Enter coupon",
                "applyCouponLabel", "Apply",
                "cartTotalLabel", "Cart Total",
                "subtotalLabel", "Subtotal",
                "totalSummaryLabel", "Total",
                "checkoutLabel", "Checkout",
                "continueShoppingLabel", "Continue Shopping",
                "updateCartLabel", "Update Cart",
                "continueShoppingLink", "/content/home",
                "updateCartLink", "/content/cart",
                "checkoutLink", "/content/checkout"
        );

        context.currentResource("/content/test");
    }

    /* =========================
       MODEL INITIALIZATION
    ========================= */
    @Test
    void testModelInitialization() {

        ProductCartModel model = context.currentResource().adaptTo(ProductCartModel.class);

        assertNotNull(model);
    }

    /* =========================
       LABEL VALUES TEST
    ========================= */
    @Test
    void testLabels() {

        ProductCartModel model = context.currentResource().adaptTo(ProductCartModel.class);

        assertEquals("Products", model.getProductsLabel());
        assertEquals("Price", model.getPriceLabel());
        assertEquals("Quantity", model.getQuantityLabel());
        assertEquals("Total", model.getTotalLabel());
    }

    /* =========================
       COUPON VALUES TEST
    ========================= */
    @Test
    void testCouponFields() {

        ProductCartModel model = context.currentResource().adaptTo(ProductCartModel.class);

        assertEquals("Enter coupon", model.getCouponPlaceholder());
        assertEquals("Apply", model.getApplyCouponLabel());
    }

    /* =========================
       SUMMARY VALUES TEST
    ========================= */
    @Test
    void testSummaryFields() {

        ProductCartModel model = context.currentResource().adaptTo(ProductCartModel.class);

        assertEquals("Cart Total", model.getCartTotalLabel());
        assertEquals("Subtotal", model.getSubtotalLabel());
        assertEquals("Total", model.getTotalSummaryLabel());
    }

    /* =========================
       BUTTON & LINKS TEST
    ========================= */
    @Test
    void testButtonsAndLinks() {

        ProductCartModel model = context.currentResource().adaptTo(ProductCartModel.class);

        assertEquals("Checkout", model.getCheckoutLabel());
        assertEquals("Continue Shopping", model.getContinueShoppingLabel());
        assertEquals("Update Cart", model.getUpdateCartLabel());

        assertEquals("/content/home", model.getContinueShoppingLink());
        assertEquals("/content/cart", model.getUpdateCartLink());
        assertEquals("/content/checkout", model.getCheckoutLink());
    }

    /* =========================
       NULL SAFETY TEST
    ========================= */
    @Test
    void testNullValues() {

        context.create().resource("/content/empty");
        context.currentResource("/content/empty");

        ProductCartModel model = context.currentResource().adaptTo(ProductCartModel.class);

        assertNotNull(model);

        assertNull(model.getProductsLabel());
        assertNull(model.getPriceLabel());
        assertNull(model.getQuantityLabel());
        assertNull(model.getTotalLabel());
    }
}