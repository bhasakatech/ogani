package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HeroBannerModel}.
 * <p>
 * These tests validate that authored properties are correctly injected into the model
 * and that missing properties are handled gracefully without throwing errors.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class HeroBannerModelTest {

    private static final Logger LOG = LoggerFactory.getLogger(HeroBannerModelTest.class);

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroBannerModel.class);
        LOG.info("Registered HeroBannerModel with AEM context.");
    }

    /**
     * Verifies that properties from a JSON fixture are injected correctly into the model.
     */
    @Test
    void testPropertiesInjectedCorrectly() {
        try {
            context.load().json("/HeroBanner.json", "/content/heroBanner");
            Resource root = context.resourceResolver().getResource("/content/heroBanner");
            HeroBannerModel model = root.adaptTo(HeroBannerModel.class);

            assertEquals("Fresh Veggies", model.getEyebrowText());
            assertEquals("<strong>100% Organic</strong>", model.getHeroDesc());
            assertEquals("Healthy and affordable", model.getSubText());

            LOG.info("testPropertiesInjectedCorrectly passed: eyebrowText={}, heroDesc={}, subText={}",
                    model.getEyebrowText(), model.getHeroDesc(), model.getSubText());
        } catch (Exception e) {
            LOG.error("Error in testPropertiesInjectedCorrectly", e);
            throw e;
        }
    }

    /**
     * Verifies that missing properties are handled gracefully (returning null).
     */
    @Test
    void testMissingPropertiesHandledGracefully() {
        try {
            Resource root = context.create().resource("/content/emptyHeroBanner");
            HeroBannerModel model = root.adaptTo(HeroBannerModel.class);

            assertNull(model.getEyebrowText());
            assertNull(model.getHeroDesc());
            assertNull(model.getSubText());

            LOG.info("testMissingPropertiesHandledGracefully passed: all properties returned null as expected.");
        } catch (Exception e) {
            LOG.error("Error in testMissingPropertiesHandledGracefully", e);
            throw e;
        }
    }
}
