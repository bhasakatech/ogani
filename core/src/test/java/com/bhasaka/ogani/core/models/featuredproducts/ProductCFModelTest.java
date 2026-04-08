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
class ProductCFModelTest {

    private final AemContext context = new AemContext();

    private ProductCFModel model;

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

    @Test
    void testAllFields() {
        assertNotNull(model);

        assertEquals("Apple", model.getTitle());
        assertEquals("100", model.getPrice());
        assertEquals("/content/dam/apple.png", model.getImage());
        assertArrayEquals(new String[]{"fruits", "fresh"}, model.getCategory());
    }

}