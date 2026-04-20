package com.bhasaka.ogani.core.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test class for ProductCart model.
 *
 * This class verifies the behavior of ProductCart using AEM Mock context
 * and JSON-based test data.
 *
 * Coverage includes:
 * - Adaptation of Resource to ProductCart model
 * - Mapping of all product fields from JSON
 * - Handling of missing properties
 * - Handling of partial data scenarios
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR environment
 * - Loads JSON data and maps it to resource properties
 * - Adapts resource to ProductCart model
 * - Validates field values using assertions
 *
 * Goal:
 * Ensure correct mapping of product properties and proper handling
 * of default and partial values.
 */
@ExtendWith(AemContextExtension.class)
class ProductCartTest {

    /**
     * AEM context used to mock Sling environment and resources.
     */
    private final AemContext context = new AemContext();

    /**
     * Initializes test data before each test.
     *
     * Reads JSON data and creates a resource with corresponding properties.
     */
    @BeforeEach
    void setUp() throws Exception {

        context.addModelsForClasses(ProductCart.class);

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("cp-product.json");

        JsonNode root = mapper.readTree(is);
        JsonNode p = root.get("product");

        context.currentResource(
                context.create().resource("/content/product",
                        "title", p.get("title").asText(),
                        "category", p.get("category").asText(),
                        "image", p.get("image").asText(),
                        "originalPrice", p.get("originalPrice").asDouble(),
                        "currentPrice", p.get("currentPrice").asDouble(),
                        "discount", p.get("discount").asDouble()
                )
        );
    }

    /**
     * Tests full JSON data mapping.
     *
     * Verifies that all product fields are correctly populated.
     */
    @Test
    void testFullJsonData() {

        ProductCart model =
                context.currentResource().adaptTo(ProductCart.class);

        assertNotNull(model);
        assertEquals("Apple", model.getTitle());
        assertEquals("Fruits", model.getCategory());
        assertEquals("/content/dam/apple.png", model.getImage());
        assertEquals(100.0, model.getOriginalPrice());
        assertEquals(80.0, model.getCurrentPrice());
        assertEquals(20.0, model.getDiscount());
    }

    /**
     * Tests behavior when JSON fields are missing.
     *
     * Verifies that:
     * - Model is created successfully
     * - String fields are null
     * - Numeric fields default to 0.0
     */
    @Test
    void testMissingJsonFields() {

        context.currentResource(
                context.create().resource("/content/product2")
        );

        ProductCart model =
                context.currentResource().adaptTo(ProductCart.class);

        assertNotNull(model);
        assertNull(model.getTitle());
        assertNull(model.getCategory());
        assertNull(model.getImage());
        assertEquals(0.0, model.getOriginalPrice());
        assertEquals(0.0, model.getCurrentPrice());
        assertEquals(0.0, model.getDiscount());
    }

    /**
     * Tests partial JSON data mapping.
     *
     * Verifies that:
     * - Available fields are populated correctly
     * - Missing fields use default values
     */
    @Test
    void testPartialJsonData() {

        context.currentResource(
                context.create().resource("/content/product3",
                        "title", "Milk",
                        "originalPrice", 50.0
                )
        );

        ProductCart model =
                context.currentResource().adaptTo(ProductCart.class);

        assertNotNull(model);
        assertEquals("Milk", model.getTitle());
        assertNull(model.getCategory());
        assertNull(model.getImage());
        assertEquals(50.0, model.getOriginalPrice());
        assertEquals(0.0, model.getCurrentPrice());
        assertEquals(0.0, model.getDiscount());
    }
}