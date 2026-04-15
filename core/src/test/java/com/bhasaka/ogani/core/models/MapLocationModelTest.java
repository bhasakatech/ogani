package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Unit tests for the {@link MapLocationModel} model.
 */
@ExtendWith(AemContextExtension.class)
class MapLocationModelTest {

    public final AemContext ctx = new AemContext();

    /**
     * Loads the map location fixtures and registers the model class.
     */
    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(MapLocationModel.class);
        ctx.load().json("/mapLocationTest.json", "/content/map");
    }

    /**
     * Verifies the model resolves all properties from the populated fixture.
     */
    @Test
    void testGetters_HappyPath() {
        Resource resource = ctx.resourceResolver().getResource("/content/map/happy-path");
        assertNotNull(resource, "Resource is null! Check JSON path.");

        MapLocationModel model = resource.adaptTo(MapLocationModel.class);
        assertNotNull(model, "Model should adapt successfully");

        assertEquals("40.7128", model.getLatitude());
        assertEquals("-74.0060", model.getLongitude());
        assertEquals("Ogani HQ", model.getTitle());
        assertEquals("+1 555 123 4567", model.getPhone());
        assertEquals("123 Main St, NY", model.getAddress());
        assertTrue(model.isHasWidgetDetails(), "Widget details should be true if any detail field is present");
    }

    /**
     * Verifies default latitude and longitude values are used when the content is empty.
     */
    @Test
    void testInit_EmptyPath_Fallbacks() {
        Resource resource = ctx.resourceResolver().getResource("/content/map/empty-path");
        assertNotNull(resource);

        MapLocationModel model = resource.adaptTo(MapLocationModel.class);
        assertNotNull(model);

        assertEquals("17.387140", model.getLatitude());
        assertEquals("78.491684", model.getLongitude());

        assertNull(model.getTitle());
        assertNull(model.getPhone());
        assertNull(model.getAddress());
        assertFalse(model.isHasWidgetDetails(), "Widget details should be false if all detail fields are missing");
    }

    /**
     * Verifies blank latitude and longitude values fall back to defaults.
     */
    @Test
    void testInit_BlankLatLon_Fallbacks() {
        Resource resource = ctx.resourceResolver().getResource("/content/map/blank-lat-lon");
        assertNotNull(resource);

        MapLocationModel model = resource.adaptTo(MapLocationModel.class);

        assertEquals("17.387140", model.getLatitude());
        assertEquals("78.491684", model.getLongitude());
    }


    /**
     * Verifies widget details are reported when only the title is present.
     */
    @Test
    void testWidgetDetails_OnlyTitle() {
        Resource resource = ctx.resourceResolver().getResource("/content/map/only-title");
        MapLocationModel model = resource.adaptTo(MapLocationModel.class);

        assertTrue(model.isHasWidgetDetails(), "Should be true if ONLY title is provided");
    }

    /**
     * Verifies widget details are reported when only the phone is present.
     */
    @Test
    void testWidgetDetails_OnlyPhone() {
        Resource resource = ctx.resourceResolver().getResource("/content/map/only-phone");
        MapLocationModel model = resource.adaptTo(MapLocationModel.class);

        assertTrue(model.isHasWidgetDetails(), "Should be true if ONLY phone is provided");
    }

    /**
     * Verifies widget details are reported when only the address is present.
     */
    @Test
    void testWidgetDetails_OnlyAddress() {
        Resource resource = ctx.resourceResolver().getResource("/content/map/only-address");
        MapLocationModel model = resource.adaptTo(MapLocationModel.class);

        assertTrue(model.isHasWidgetDetails(), "Should be true if ONLY address is provided");
    }
}