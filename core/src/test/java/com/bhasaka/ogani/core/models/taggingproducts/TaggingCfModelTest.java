package com.bhasaka.ogani.core.models.taggingproducts;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(AemContextExtension.class)
class TaggingCfModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(TaggingCfModel.class);

        context.create().resource("/content/product",
                "title", "Apple",
                "price", "100",
                "image", "/content/dam/apple.png",
                "cq:tags", new String[]{"fruit", "fresh"}
        );

        context.currentResource("/content/product");
    }

    @Test
    void testValidProduct() {
        TaggingCfModel model = context.currentResource().adaptTo(TaggingCfModel.class);

        assertNotNull(model);
        assertEquals("Apple", model.getTitle());
        assertEquals("100", model.getPrice());
        assertEquals("/content/dam/apple.png", model.getImage());
        assertEquals(2, model.getCategories().length);
    }

    @Test
    void testNullProduct() {
        context.create().resource("/content/empty");
        context.currentResource("/content/empty");

        TaggingCfModel model = context.currentResource().adaptTo(TaggingCfModel.class);

        assertNotNull(model);
        assertEquals("", model.getTitle());
        assertEquals("", model.getPrice());
        assertEquals("", model.getImage());
        assertEquals(0, model.getCategories().length);
    }

    @Test
    void testPartialProduct() {
        context.create().resource("/content/partial",
                "title", "Banana"
        );
        context.currentResource("/content/partial");

        TaggingCfModel model = context.currentResource().adaptTo(TaggingCfModel.class);
        assertNotNull(model);
        assertEquals("Banana", model.getTitle());
        assertEquals("", model.getPrice());
        assertEquals("", model.getImage());
        assertEquals(0, model.getCategories().length);
    }
}
