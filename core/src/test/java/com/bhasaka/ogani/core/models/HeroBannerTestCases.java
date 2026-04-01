package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HeroBannerModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroBannerModel.class);
    }

    @Test
    void testPropertiesInjectedCorrectly() {
        context.load().json("/HeroBanner.json", "/content/heroBanner");
        Resource root = context.resourceResolver().getResource("/content/heroBanner");
        HeroBannerModel model = root.adaptTo(HeroBannerModel.class);

        assertEquals("Fresh Veggies", model.getEyebrowText());
        assertEquals("<strong>100% Organic</strong>", model.getHeroDesc());
        assertEquals("Healthy and affordable", model.getSubText());
    }

    @Test
    void testMissingPropertiesHandledGracefully() {
        Resource root = context.create().resource("/content/emptyHeroBanner");
        HeroBannerModel model = root.adaptTo(HeroBannerModel.class);

        assertNull(model.getEyebrowText());
        assertNull(model.getHeroDesc());
        assertNull(model.getSubText());
    }
}