package com.bhasaka.ogani.core.models.productCarousel;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test class for Product Sling Model.
 *
 * This class verifies the behavior of the Product model using AEM Mock context.
 *
 * Coverage includes:
 * - Adaptation of Resource to Product model
 * - Getter methods populated from JCR properties
 * - Setter methods and value updates
 * - Default values when properties are missing
 * - Partial property injection scenarios
 * - Handling of null, empty, and negative values
 * - Overwriting existing values
 * - Adaptation failure for invalid resource paths
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR environment
 * - Creates mock resources with properties
 * - Adapts resources to Product model
 * - Validates behavior using assertions
 *
 * Goal:
 * Ensure full coverage of getters, setters, and edge cases in the Product model.
 */
@ExtendWith(AemContextExtension.class)
class ProductTest {

    /**
     * AEM context used to mock Sling environment and resources.
     */
    private final AemContext context = new AemContext();

    /**
     * Product model instance used across tests.
     */
    private Product product;

    /**
     * Initializes test data before each test.
     *
     * Registers the Product model and creates a resource with
     * complete product properties for testing.
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(Product.class);

        context.create().resource("/content/product",
                "title", "Apple",
                "category", "Fruits",
                "image", "/content/dam/images/apple.png",
                "originalPrice", 100.0,
                "currentPrice", 80.0,
                "discount", 20.0
        );

        Resource resource = context.resourceResolver().getResource("/content/product");
        product = resource.adaptTo(Product.class);
    }

    /**
     * Verifies that the Product model is successfully adapted from resource.
     */
    @Test
    void testModel_NotNull() {
        assertNotNull(product);
    }

    /**
     * Tests getter methods when values are injected from resource.
     *
     * Verifies that all properties are correctly mapped from JCR.
     */
    @Test
    void testGetters_FromResource() {

        assertEquals("Apple", product.getTitle());
        assertEquals("Fruits", product.getCategory());
        assertEquals("/content/dam/images/apple.png", product.getImage());

        assertEquals(100.0, product.getOriginalPrice());
        assertEquals(80.0, product.getCurrentPrice());
        assertEquals(20.0, product.getDiscount());
    }

    /**
     * Tests setter methods with normal values.
     *
     * Verifies that updated values are reflected correctly.
     */
    @Test
    void testSetters() {

        product.setTitle("Mango");
        product.setCategory("Tropical");
        product.setImage("/content/dam/images/mango.png");
        product.setOriginalPrice(200.0);
        product.setCurrentPrice(150.0);
        product.setDiscount(25);

        assertEquals("Mango", product.getTitle());
        assertEquals("Tropical", product.getCategory());
        assertEquals("/content/dam/images/mango.png", product.getImage());

        assertEquals(200.0, product.getOriginalPrice());
        assertEquals(150.0, product.getCurrentPrice());
        assertEquals(25.0, product.getDiscount());
    }

    /**
     * Tests default values when no properties are present in resource.
     *
     * Verifies that:
     * - String fields are null
     * - Numeric fields default to 0.0
     */
    @Test
    void testDefaultValues_WhenPropertiesMissing() {

        context.create().resource("/content/emptyProduct");

        Resource resource = context.resourceResolver().getResource("/content/emptyProduct");
        Product emptyProduct = resource.adaptTo(Product.class);

        assertNotNull(emptyProduct);

        assertNull(emptyProduct.getTitle());
        assertNull(emptyProduct.getCategory());
        assertNull(emptyProduct.getImage());

        assertEquals(0.0, emptyProduct.getOriginalPrice());
        assertEquals(0.0, emptyProduct.getCurrentPrice());
        assertEquals(0.0, emptyProduct.getDiscount());
    }

    /**
     * Tests partial property injection.
     *
     * Verifies that:
     * - Available properties are injected correctly
     * - Missing properties use default values
     */
    @Test
    void testPartialValues() {

        context.create().resource("/content/partialProduct",
                "title", "Banana",
                "originalPrice", 50.0
        );

        Resource resource = context.resourceResolver().getResource("/content/partialProduct");
        Product partialProduct = resource.adaptTo(Product.class);

        assertEquals("Banana", partialProduct.getTitle());
        assertNull(partialProduct.getCategory());

        assertEquals(50.0, partialProduct.getOriginalPrice());
        assertEquals(0.0, partialProduct.getCurrentPrice());
        assertEquals(0.0, partialProduct.getDiscount());
    }

    /**
     * Tests setter behavior with null and empty values.
     *
     * Verifies that:
     * - Null values are accepted
     * - Empty strings are stored correctly
     */
    @Test
    void testSetters_NullAndEmptyValues() {

        product.setTitle(null);
        product.setCategory("");
        product.setImage(null);

        assertNull(product.getTitle());
        assertEquals("", product.getCategory());
        assertNull(product.getImage());
    }

    /**
     * Tests setter behavior with negative and zero values.
     *
     * Verifies that numeric fields accept edge values.
     */
    @Test
    void testSetters_NegativeAndZeroValues() {

        product.setOriginalPrice(-100.0);
        product.setCurrentPrice(0.0);
        product.setDiscount(-10.0);

        assertEquals(-100.0, product.getOriginalPrice());
        assertEquals(0.0, product.getCurrentPrice());
        assertEquals(-10.0, product.getDiscount());
    }

    /**
     * Tests overwriting of values using setters.
     *
     * Verifies that the latest value replaces the previous one.
     */
    @Test
    void testSetters_OverwriteValues() {

        product.setTitle("Apple");
        product.setTitle("Orange");

        assertEquals("Orange", product.getTitle());
    }

    /**
     * Tests adaptation failure when resource does not exist.
     *
     * Verifies that adapting a null resource returns null.
     */
    @Test
    void testAdaptTo_NullResource() {

        Resource resource = context.resourceResolver().getResource("/invalid/path");

        Product result = null;
        if (resource != null) {
            result = resource.adaptTo(Product.class);
        }

        assertNull(result);
    }
}