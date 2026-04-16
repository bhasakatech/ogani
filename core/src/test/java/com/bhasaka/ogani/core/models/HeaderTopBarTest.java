package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Unit tests for the {@link HeaderTopBar} model.
 */
@ExtendWith(AemContextExtension.class)
class HeaderTopBarTest {

    public final AemContext ctx = new AemContext();

    /**
     * Loads the header top bar fixtures and registers the model package.
     */
    @BeforeEach
    void setUp() {
       ctx.addModelsForPackage("com.bhasaka.ogani.core.models");
        ctx.load().json("/headerTopBarTest.json", "/content/ogani/headertopbar");
    }

    /**
     * Verifies all header top bar properties are read from a fully populated resource.
     */
    @Test
    void testGetters_HappyPath() {
        String resourcePath = "/content/ogani/headertopbar/happy-path";
        Resource resource = ctx.resourceResolver().getResource(resourcePath);

        assertNotNull(resource, "Resource is null! Check if HeaderTopBarTest.json is loaded correctly.");

        HeaderTopBar topBar = resource.adaptTo(HeaderTopBar.class);

        assertNotNull(topBar, "Model failed to adapt.");
        assertEquals("support@ogani.com", topBar.getEmail());
        assertEquals("Free Shipping on all orders over $99!", topBar.getPromotionalText());
        assertEquals("fa-user", topBar.getLoginIcon());
        assertEquals("/content/ogani/us/en/login.html", topBar.getLoginLink());
        assertArrayEquals(new String[]{"English", "Spanish", "French"}, topBar.getLanguages());

        assertNotNull(topBar.getSocialIcons(), "Social icons list is null! Adaptation of child nodes failed.");
        assertFalse(topBar.getSocialIcons().isEmpty(), "Social icons list should not be empty");
        assertEquals(2, topBar.getSocialIcons().size(), "Should load 2 items from the iconList JSON node");
    }

    /**
     * Verifies the model adapts safely when optional properties are absent.
     */
    @Test
    void testGetters_EmptyPath() {
        String resourcePath = "/content/ogani/headertopbar/empty-path";
        Resource resource = ctx.resourceResolver().getResource(resourcePath);

        assertNotNull(resource, "Resource is null! Check if HeaderTopBarTest.json is loaded correctly.");

        HeaderTopBar topBar = resource.adaptTo(HeaderTopBar.class);

        assertNotNull(topBar, "Model should adapt successfully even with no properties");
        assertNull(topBar.getEmail());
        assertNull(topBar.getPromotionalText());
        assertNull(topBar.getLoginIcon());
        assertNull(topBar.getLoginLink());
        assertNull(topBar.getLanguages());
        assertNull(topBar.getSocialIcons(), "Child resource list should be null if the node doesn't exist");
    }
}