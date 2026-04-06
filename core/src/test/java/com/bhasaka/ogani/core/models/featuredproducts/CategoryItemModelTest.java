package com.bhasaka.ogani.core.models.featuredproducts;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CategoryItemModelTest {

    private final AemContext context = new AemContext();

    private CategoryItemModel model;

    @BeforeEach
    void setUp() {
        // Create mock resource with property
        Resource resource = context.create().resource("/content/test",
                "categoryTag", "fruits");

        // Adapt to model
        model = resource.adaptTo(CategoryItemModel.class);
    }

    @Test
    void testGetCategoryTag() {
        assertNotNull(model);
        assertEquals("fruits", model.getCategoryTag());
    }

}