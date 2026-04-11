package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.wcm.testing.mock.aem.junit5.AemContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({
        AemContextExtension.class,
        MockitoExtension.class})
class CheckoutModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CheckoutModel.class);
        context.load().json("/checkout.json", "/content");
        context.create().resource("/test",
                "cartPath", "/content/cart");
    }

    @Test
    void testInit_success() {

        Resource resource = context.resourceResolver().getResource("/test");
        assertNotNull(resource);
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertNotNull(model);
        assertEquals(1, model.getProducts().size());
        assertEquals(150.0, model.getSubtotal());
        assertFalse(model.isEmpty());
    }

    @Test
    void testInit_cartPathMissing() {

        Resource resource = context.create().resource("/noCart");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testInit_cartNotFound() {

        Resource resource = context.create().resource("/wrong",
                "cartPath", "/invalid/path");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testGetters_defaultValues() {

        Resource resource = context.resourceResolver().getResource("/test");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertEquals("Your Order", model.getOrderTitle());
        assertEquals("Subtotal", model.getSubtotalLabel());
        assertEquals("Total", model.getFinalTotalLabel());
        assertEquals("PLACE ORDER", model.getPlaceOrderLabel());
    }

    @Test
    void testGetTotal() {

        Resource resource = context.resourceResolver().getResource("/test");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertEquals(model.getSubtotal(), model.getTotal());
    }

    @Test
    void testIsEmpty_false() {

        Resource resource = context.resourceResolver().getResource("/test");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertFalse(model.isEmpty());
    }

    @Test
    void testIsEmpty_true() {

        Resource resource = context.create().resource("/empty",
                "cartPath", "/content/emptyCart");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);
        assertTrue(model.isEmpty());
    }
}