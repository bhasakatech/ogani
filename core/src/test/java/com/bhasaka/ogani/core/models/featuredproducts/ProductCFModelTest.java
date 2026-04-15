package com.bhasaka.ogani.core.models.featuredproducts;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ProductCFModel}.
 * <p>
 * This class verifies that the Sling Model correctly adapts
 * from a resource and retrieves all product content fragment fields.
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductCFModelTest {

    private final AemContext context = new AemContext();

    private ProductCFModel model;

    /**
     * Sets up the test resource with product properties
     * and adapts it to the model.
     */
    @BeforeEach
    void setUp() {
        Resource resource = context.create().resource("/content/test",
                "title", "Apple",
                "price", "100",
                "image", "/content/dam/apple.png",
                "category", new String[]{"fruits", "fresh"}
        );

        model = resource.adaptTo(ProductCFModel.class);
    }

    /**
     * Verifies that all fields are correctly retrieved from the model.
     */
    @Test
    void testAllFields() {
        assertNotNull(model);

        assertEquals("Apple", model.getTitle());
        assertEquals("100", model.getPrice());
        assertEquals("/content/dam/apple.png", model.getImage());
        assertArrayEquals(new String[]{"fruits", "fresh"}, model.getCategory());
    }
}