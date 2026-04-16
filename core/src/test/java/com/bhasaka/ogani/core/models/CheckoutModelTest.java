package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.wcm.testing.mock.aem.junit5.AemContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for {@link CheckoutModel}.
 * <p>
 * This class validates cart product loading, subtotal calculation,
 * default values, and edge cases such as missing or invalid cart paths
 * using AEM Mocks.
 */
@ExtendWith({
        AemContextExtension.class,
        MockitoExtension.class})
class CheckoutModelTest {

    private final AemContext context = new AemContext();

    /**
     * Sets up test context by registering model,
     * loading JSON data, and creating test resource.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CheckoutModel.class);
        context.load().json("/checkout.json", "/content");
        context.create().resource("/test",
                "cartPath", "/content/cart");
    }

    /**
     * Verifies successful initialization of model,
     * including product loading and subtotal calculation.
     */
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

    /**
     * Validates behavior when cartPath is missing.
     */
    @Test
    void testInit_cartPathMissing() {

        Resource resource = context.create().resource("/noCart");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);

        assertTrue(model.getProducts().isEmpty());
    }

    /**
     * Validates behavior when cart path is invalid or not found.
     */
    @Test
    void testInit_cartNotFound() {

        Resource resource = context.create().resource("/wrong",
                "cartPath", "/invalid/path");

        CheckoutModel model = resource.adaptTo(CheckoutModel.class);

        assertTrue(model.getProducts().isEmpty());
    }

    /**
     * Verifies default label values when not configured.
     */
    @Test
    void testGetters_defaultValues() {

        Resource resource = context.resourceResolver().getResource("/test");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);

        assertEquals("Your Order", model.getOrderTitle());
        assertEquals("Subtotal", model.getSubtotalLabel());
        assertEquals("Total", model.getFinalTotalLabel());
        assertEquals("PLACE ORDER", model.getPlaceOrderLabel());
    }

    /**
     * Validates total calculation equals subtotal.
     */
    @Test
    void testGetTotal() {

        Resource resource = context.resourceResolver().getResource("/test");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);

        assertEquals(model.getSubtotal(), model.getTotal());
    }

    /**
     * Verifies isEmpty() returns false when products exist.
     */
    @Test
    void testIsEmpty_false() {

        Resource resource = context.resourceResolver().getResource("/test");
        CheckoutModel model = resource.adaptTo(CheckoutModel.class);

        assertFalse(model.isEmpty());
    }

    /**
     * Verifies isEmpty() returns true when no products are present.
     */
    @Test
    void testIsEmpty_true() {

        Resource resource = context.create().resource("/empty",
                "cartPath", "/content/emptyCart");

        CheckoutModel model = resource.adaptTo(CheckoutModel.class);

        assertTrue(model.isEmpty());
    }
}