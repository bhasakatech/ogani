package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test class for ProductCartModel.
 *
 * This class verifies the behavior of ProductCartModel using AEM Mock context
 * and JSON-based content.
 *
 * Coverage includes:
 * - Initialization of ProductCartModel from resource
 * - Population of product list
 * - Validation of product data inside the list
 * - Verification of label fields
 * - Handling of empty product scenarios
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR environment
 * - Loads structured JSON to represent component data
 * - Adapts resource to ProductCartModel
 * - Validates output using assertions
 *
 * Goal:
 * Ensure correct mapping of product list and component properties,
 * along with proper handling of empty data cases.
 */
@ExtendWith(AemContextExtension.class)
class ProductCartModelTest {

    /**
     * AEM context used to mock Sling environment and repository.
     */
    private final AemContext context = new AemContext();

    /**
     * Initializes test data before each test.
     *
     * Loads JSON content and sets the current resource.
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(ProductCartModel.class, ProductCart.class);

        context.load().json("/cp-product-model.json", "/content");

        context.currentResource("/content/test");
    }

    /**
     * Tests model initialization.
     *
     * Verifies that:
     * - Model is created successfully
     * - Product list is populated
     * - isEmpty returns false
     */
    @Test
    void testModelInitialization() {

        ProductCartModel model =
                context.currentResource().adaptTo(ProductCartModel.class);

        assertNotNull(model);
        assertFalse(model.isEmpty());
        assertEquals(2, model.getProductList().size());
    }

    /**
     * Tests product values inside the product list.
     *
     * Verifies that product fields are correctly mapped.
     */
    @Test
    void testProductValues() {

        ProductCartModel model =
                context.currentResource().adaptTo(ProductCartModel.class);

        ProductCart product = model.getProductList().get(0);

        assertEquals("Apple", product.getTitle());
        assertEquals("Fruits", product.getCategory());
        assertEquals("/content/dam/apple.png", product.getImage());
        assertEquals(80.0, product.getCurrentPrice());
    }

    /**
     * Tests label fields in the model.
     *
     * Verifies that component labels are correctly injected.
     */
    @Test
    void testLabels() {

        ProductCartModel model =
                context.currentResource().adaptTo(ProductCartModel.class);

        assertEquals("Products", model.getProductsLabel());
        assertEquals("Price", model.getPriceLabel());
        assertEquals("Quantity", model.getQuantityLabel());
        assertEquals("Total", model.getTotalLabel());
    }

    /**
     * Tests behavior when no products are available.
     *
     * Verifies that:
     * - Model is created successfully
     * - isEmpty returns true
     */
    @Test
    void testEmptyWhenNoProducts() {

        context.create().resource("/content/empty");
        context.currentResource("/content/empty");

        ProductCartModel model =
                context.currentResource().adaptTo(ProductCartModel.class);

        assertNotNull(model);
        assertTrue(model.isEmpty());
    }
}