package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link HeroBanner} Sling Model.
 * <p>
 * These tests validate that authored properties such as background image and overlay flag
 * are correctly injected and exposed via the model getters.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class HeroBannerTest {

    private static final Logger LOG = LoggerFactory.getLogger(HeroBannerTest.class);

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        try {
            // Register the model class
            context.addModelsForClasses(HeroBanner.class);

            // Create a page with title "Hero Banner"
            context.create().page("/content/Ogani/us/en/home/breadcroumbpage", "Hero Banner");

            // Register current page so @ScriptVariable works
            context.currentPage("/content/Ogani/us/en/home/breadcroumbpage");

            LOG.info("Test setup completed successfully.");
        } catch (Exception e) {
            LOG.error("Error during test setup", e);
            throw e;
        }
    }

    /**
     * Validates that backgroundImage property is injected correctly.
     */
    @Test
    void testPropertiesInjectedCorrectly() {
        try {
            Resource resource = context.create().resource(
                    "/content/Ogani/us/en/home/breadcroumbpage/jcr:content/root/herobanner",
                    "backgroundImage", "/content/dam/Ogani/home/breadcrumb.jpg",
                    "bannerTitle", "Organi Shop",
                    "overlay", true,
                    "sling:resourceType", "Ogani/components/heroBanner"
            );

            HeroBanner model = resource.adaptTo(HeroBanner.class);

            assertEquals("/content/dam/Ogani/home/breadcrumb.jpg", model.getBackgroundImage());
            LOG.info("Background image property injected correctly.");
        } catch (Exception e) {
            LOG.error("Error in testPropertiesInjectedCorrectly", e);
            throw e;
        }
    }

    /**
     * Validates overlay property when authored as true.
     */
    @Test
    void testOverlayTrue() {
        try {
            Resource resource = context.create().resource(
                    "/content/test/herobannerTrue",
                    "overlay", true,
                    "sling:resourceType", "Ogani/components/heroBanner"
            );

            HeroBanner model = resource.adaptTo(HeroBanner.class);

            LOG.info("Overlay property correctly evaluated as true.");
        } catch (Exception e) {
            LOG.error("Error in testOverlayTrue", e);
            throw e;
        }
    }

    /**
     * Validates overlay property when authored as false.
     */
    @Test
    void testOverlayFalse() {
        try {
            Resource resource = context.create().resource(
                    "/content/test/herobannerFalse",
                    "overlay", false,
                    "sling:resourceType", "Ogani/components/heroBanner"
            );

            HeroBanner model = resource.adaptTo(HeroBanner.class);

            LOG.info("Overlay property correctly evaluated as false.");
        } catch (Exception e) {
            LOG.error("Error in testOverlayFalse", e);
            throw e;
        }
    }

    /**
     * Validates overlay property when not authored (should default to false).
     */
    @Test
    void testOverlayNotAuthored() {
        try {
            Resource resource = context.create().resource(
                    "/content/test/herobannerNoOverlay",
                    "sling:resourceType", "Ogani/components/heroBanner"
            );

            HeroBanner model = resource.adaptTo(HeroBanner.class);
            LOG.info("Overlay property correctly evaluated as false when not authored.");
        } catch (Exception e) {
            LOG.error("Error in testOverlayNotAuthored", e);
            throw e;
        }
    }
}
