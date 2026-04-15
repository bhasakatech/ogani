package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.productCarousel.Product;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for RelatedProducts model.
 *
 * This class verifies:
 * - Product mapping from JSON
 * - Handling of invalid and null product paths
 * - Filtering logic
 * - Section title injection
 *
 * Uses AEM Mock context with JSON data.
 */
/**
 * Unit test class for RelatedProducts model.
 *
 * This class verifies the behavior of RelatedProducts using AEM Mock context
 * with JSON-based test data.
 *
 * Coverage includes:
 * - Adaptation of Resource to RelatedProducts model
 * - Injection of sectionTitle property
 * - Mapping of product references to Product objects
 * - Validation of product data such as title, category, and image
 * - Filtering of invalid or empty product paths
 * - Handling of empty resource scenarios
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR repository
 * - Loads structured JSON data to represent component and DAM content
 * - Adapts resource to RelatedProducts model
 * - Validates results using assertions
 *
 * Goal:
 * Ensure correct resolution of product paths, proper filtering of invalid entries,
 * and accurate mapping of product data.
 */
@ExtendWith(AemContextExtension.class)
class RelatedProductsTest {

    /**
     * AEM context used to mock Sling environment and repository.
     */
    private final AemContext context = new AemContext();

    /**
     * ResourceResolver injected for completeness.
     * Not directly used in test logic.
     */
    @SlingObject
    private ResourceResolver resolver;

    /**
     * Model instance under test.
     */
    private RelatedProducts model;

    /**
     * Initializes test setup before each test.
     *
     * Steps:
     * - Registers model classes
     * - Loads JSON test data into mock repository
     * - Sets current resource
     * - Adapts resource to RelatedProducts model
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(RelatedProducts.class, Product.class);

        context.load().json("/related-products.json", "/content");

        context.currentResource("/content/test");

        model = context.currentResource().adaptTo(RelatedProducts.class);
    }

    /**
     * Verifies that the model is successfully created.
     */
    @Test
    void testModel_NotNull() {
        assertNotNull(model);
    }

    /**
     * Tests sectionTitle property injection.
     *
     * Verifies that the value is correctly mapped from resource.
     */
    @Test
    void testSectionTitle() {
        assertEquals("Related Products", model.getSectionTitle());
    }

    /**
     * Tests that valid products are loaded correctly.
     *
     * Verifies that:
     * - Product list is not null
     * - Only valid product entries are included
     */
    @Test
    void testProductsLoaded() {

        List<Product> products = model.getProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
    }

    /**
     * Tests values of mapped Product objects.
     *
     * Verifies that:
     * - Product properties such as title, category, and image are correctly mapped
     * - Multiple products are correctly resolved
     */
    @Test
    void testProductValues() {

        List<Product> products = model.getProducts();

        Product p1 = products.get(0);
        Product p2 = products.get(1);

        assertEquals("Apple", p1.getTitle());
        assertEquals("Fruits", p1.getCategory());
        assertEquals("/content/dam/apple.png", p1.getImage());

        assertEquals("Banana", p2.getTitle());
        assertEquals("Fruits", p2.getCategory());
    }

    /**
     * Tests filtering of invalid product entries.
     *
     * Verifies that:
     * - Entries with empty or missing productPath are ignored
     * - Only valid products remain in the list
     */
    @Test
    void testInvalidProductsFiltered() {

        List<Product> products = model.getProducts();

        assertEquals(2, products.size());
    }

    /**
     * Tests behavior when no product resources are present.
     *
     * Verifies that:
     * - Model is created successfully
     * - Product list is null when no data is available
     */
    @Test
    void testEmptyModel() {

        context.create().resource("/content/empty");
        context.currentResource("/content/empty");

        RelatedProducts emptyModel =
                context.currentResource().adaptTo(RelatedProducts.class);

        assertNotNull(emptyModel);
        assertNull(emptyModel.getProducts());
    }
}