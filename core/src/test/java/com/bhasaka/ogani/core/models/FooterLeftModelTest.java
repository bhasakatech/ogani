package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import java.util.List;
/**
 * Unit test class for FooterLeftModel.
 *
 * This class verifies the behavior of FooterLeftModel using AEM Mock context.
 *
 * Coverage includes:
 * - Adaptation of Resource to FooterLeftModel
 * - Injection of basic fields such as logo, address, phone, and email
 * - Mapping of child resources for linksColumn1 and linksColumn2
 * - Validation of link data such as text and URL
 * - Handling of empty resource scenarios
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR environment
 * - Loads JSON data to represent realistic component structure
 * - Adapts resource to model
 * - Validates fields and child resources using assertions
 *
 * Goal:
 * Ensure correct mapping of component properties and child resources,
 * including edge cases where data is missing.
 */
@ExtendWith(AemContextExtension.class)
class FooterLeftModelTest {

    /**
     * AEM context used to mock Sling environment and repository.
     */
    private final AemContext context = new AemContext();

    /**
     * Model instance under test.
     */
    private FooterLeftModel model;

    /**
     * Initializes test data before each test.
     *
     * Loads JSON content into mock context, registers model,
     * and adapts resource to FooterLeftModel.
     */
    @BeforeEach
    void setUp() {
        context.load().json("/footer-left.json", "/content/footer");

        context.addModelsForClasses(FooterLeftModel.class);

        Resource resource = context.resourceResolver().getResource("/content/footer");
        model = resource.adaptTo(FooterLeftModel.class);
    }

    /**
     * Tests basic field injection from resource.
     *
     * Verifies that logo, address, phone, and email
     * are correctly populated from JCR.
     */
    @Test
    void testBasicFields() {
        assertNotNull(model);

        assertEquals("/content/dam/logo.png", model.getLogo());
        assertEquals("Hyderabad, India", model.getAddress());
        assertEquals("+91-9876543210", model.getPhone());
        assertEquals("test@example.com", model.getEmail());
    }

    /**
     * Tests mapping of links in the first column.
     *
     * Verifies that:
     * - linksColumn1 is correctly populated
     * - Each link contains correct text and URL
     */
    @Test
    void testLinksColumn1() {
        List<FooterLeftModel.Link> links = model.getLinksColumn1();

        assertNotNull(links);
        assertEquals(2, links.size());

        assertEquals("Home", links.get(0).getText());
        assertEquals("/home", links.get(0).getUrl());

        assertEquals("Shop", links.get(1).getText());
        assertEquals("/shop", links.get(1).getUrl());
    }

    /**
     * Tests mapping of links in the second column.
     *
     * Verifies that:
     * - linksColumn2 is correctly populated
     * - Each link contains correct text and URL
     */
    @Test
    void testLinksColumn2() {
        List<FooterLeftModel.Link> links = model.getLinksColumn2();

        assertNotNull(links);
        assertEquals(2, links.size());

        assertEquals("Contact", links.get(0).getText());
        assertEquals("/contact", links.get(0).getUrl());

        assertEquals("About", links.get(1).getText());
        assertEquals("/about", links.get(1).getUrl());
    }

    /**
     * Tests behavior when resource has no properties or child resources.
     *
     * Verifies that:
     * - Model is still created successfully
     * - All fields return null
     */
    @Test
    void testEmptyModel() {

        context.create().resource("/content/empty");

        Resource resource = context.resourceResolver().getResource("/content/empty");
        FooterLeftModel emptyModel = resource.adaptTo(FooterLeftModel.class);

        assertNotNull(emptyModel);

        assertNull(emptyModel.getLogo());
        assertNull(emptyModel.getAddress());
        assertNull(emptyModel.getPhone());
        assertNull(emptyModel.getEmail());
        assertNull(emptyModel.getLinksColumn1());
        assertNull(emptyModel.getLinksColumn2());
    }
}