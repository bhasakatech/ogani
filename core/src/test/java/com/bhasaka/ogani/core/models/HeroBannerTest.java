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

@ExtendWith(AemContextExtension.class)
class HeroBannerTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        
        // Register the model class
        context.addModelsForClasses(HeroBanner.class);

        // Create a page with title "Hero Banner"
        context.create().page("/content/Ogani/us/en/home/breadcroumbpage", "Hero Banner");

        // Register current page so @ScriptVariable works
        context.currentPage("/content/Ogani/us/en/home/breadcroumbpage");
    }

    @Test
    void testPropertiesInjectedCorrectly() {
        // Create HeroBanner resource with all properties
        Resource resource = context.create().resource(
                "/content/Ogani/us/en/home/breadcroumbpage/jcr:content/root/herobanner",
                "backgroundImage", "/content/dam/Ogani/home/breadcrumb.jpg",
                "bannerTitle", "Organi Shop",
                "overlay", true,
                "sling:resourceType", "Ogani/components/heroBanner"
        );

        HeroBanner model = resource.adaptTo(HeroBanner.class);

        assertEquals("/content/dam/Ogani/home/breadcrumb.jpg", model.getBackgroundImage());
        assertEquals("Organi Shop", model.getBannerTitle());
        assertTrue(model.getOverlay());
    }

    @Test
    void testOverlayTrue() {
        Resource resource = context.create().resource(
                "/content/test/herobannerTrue",
                "overlay", true,
                "sling:resourceType", "Ogani/components/heroBanner"
        );

        HeroBanner model = resource.adaptTo(HeroBanner.class);
        assertTrue(model.getOverlay(), "Overlay should be true when authored as true");
        
    }

    @Test
    void testOverlayFalse() {
        Resource resource = context.create().resource(
                "/content/test/herobannerFalse",
                "overlay", false,
                "sling:resourceType", "Ogani/components/heroBanner"
        );

        HeroBanner model = resource.adaptTo(HeroBanner.class);
        assertFalse(model.getOverlay(), "Overlay should be false when authored as false");
    }

    @Test
    void testOverlayNotAuthored() {
        Resource resource = context.create().resource(
                "/content/test/herobannerNoOverlay",
                "sling:resourceType", "Ogani/components/heroBanner"
        );

        HeroBanner model = resource.adaptTo(HeroBanner.class);
        assertFalse(model.getOverlay(), "Overlay should default to false when not authored");
    }


}