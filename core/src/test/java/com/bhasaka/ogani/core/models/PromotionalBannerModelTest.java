
package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class PromotionalBannerModelTest {

    private final AemContext context = new AemContext();

    private static final String CONTENT_PATH = "/content/test/banner";

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(PromotionalBannerModel.class);
    }

    @Test
    void testAllValuesPresent() {
        context.create().resource(CONTENT_PATH,
                "title", "Big Sale",
                "description", "Up to 50% off",
                "bgImage", "/content/dam/banner.jpg",
                "alignment", "RIGHT"
        );

        Resource resource = context.resourceResolver().getResource(CONTENT_PATH);
        PromotionalBannerModel model = resource.adaptTo(PromotionalBannerModel.class);

        assertNotNull(model);

        assertEquals("Big Sale", model.getTitle());
        assertEquals("Up to 50% off", model.getDescription());
        assertEquals("/content/dam/banner.jpg", model.getBgImage());
        assertEquals("right", model.getAlignment()); // trimmed + lowercase
    }

    @Test
    void testNullValuesFallback() {
        context.create().resource(CONTENT_PATH);

        Resource resource = context.resourceResolver().getResource(CONTENT_PATH);
        PromotionalBannerModel model = resource.adaptTo(PromotionalBannerModel.class);

        assertNotNull(model);

        assertEquals("", model.getTitle());
        assertEquals("", model.getDescription());
        assertEquals("", model.getBgImage());
        assertEquals("left", model.getAlignment()); // default fallback
    }

    @Test
    void testPartialValues() {
        context.create().resource(CONTENT_PATH,
                "title", "Offer Only"
        );

        Resource resource = context.resourceResolver().getResource(CONTENT_PATH);
        PromotionalBannerModel model = resource.adaptTo(PromotionalBannerModel.class);

        assertNotNull(model);

        assertEquals("Offer Only", model.getTitle());
        assertEquals("", model.getDescription());
        assertEquals("", model.getBgImage());
        assertEquals("left", model.getAlignment());
    }

    @Test
    void testAlignmentNormalization() {
        context.create().resource(CONTENT_PATH,
                "alignment", "   LEFT   "
        );

        Resource resource = context.resourceResolver().getResource(CONTENT_PATH);
        PromotionalBannerModel model = resource.adaptTo(PromotionalBannerModel.class);

        assertEquals("left", model.getAlignment());
    }

    @Test
    void testAlignmentNullDefault() {
        context.create().resource(CONTENT_PATH,
                "alignment", null
        );

        Resource resource = context.resourceResolver().getResource(CONTENT_PATH);
        PromotionalBannerModel model = resource.adaptTo(PromotionalBannerModel.class);

        assertEquals("left", model.getAlignment());
    }
}
