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
 * Unit test class for FooterNewsletterModel.
 *
 * This class verifies the behavior of FooterNewsletterModel using AEM Mock context.
 *
 * Coverage includes:
 * - Adaptation of Resource to FooterNewsletterModel
 * - Injection of basic fields such as title and description
 * - Mapping of child resources for socialLinks
 * - Validation of social link properties like url and iconClass
 * - Handling of empty resource scenarios
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR environment
 * - Loads JSON data to represent realistic component structure
 * - Adapts resource to model
 * - Validates fields and child resources using assertions
 *
 * Goal:
 * Ensure correct mapping of component properties and social links,
 * including cases where data is missing.
 */
@ExtendWith(AemContextExtension.class)
class FooterNewsletterModelTest {

    /**
     * AEM context used to mock Sling environment and repository.
     */
    private final AemContext context = new AemContext();

    /**
     * Model instance under test.
     */
    private FooterNewsletterModel model;

    /**
     * Initializes test data before each test.
     *
     * Loads JSON content into mock context, registers model,
     * and adapts resource to FooterNewsletterModel.
     */
    @BeforeEach
    void setUp() {
        context.load().json("/footer-newsletter.json", "/content/newsletter");

        context.addModelsForClasses(FooterNewsletterModel.class);

        Resource resource = context.resourceResolver().getResource("/content/newsletter");
        model = resource.adaptTo(FooterNewsletterModel.class);
    }

    /**
     * Tests basic field injection from resource.
     *
     * Verifies that title and description
     * are correctly populated from JCR.
     */
    @Test
    void testBasicFields() {
        assertNotNull(model);

        assertEquals("Subscribe to our Newsletter", model.getTitle());
        assertEquals("Get updates about latest products", model.getDescription());
    }

    /**
     * Tests mapping of social links.
     *
     * Verifies that:
     * - socialLinks list is correctly populated
     * - Each link contains correct url and icon class
     */
    @Test
    void testSocialLinks() {
        List<FooterNewsletterModel.SocialLink> links = model.getSocialLinks();

        assertNotNull(links);
        assertEquals(2, links.size());

        assertEquals("https://facebook.com", links.get(0).getUrl());
        assertEquals("fa-facebook-f", links.get(0).getIconClass());

        assertEquals("https://twitter.com", links.get(1).getUrl());
        assertEquals("fa-twitter", links.get(1).getIconClass());
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

        context.create().resource("/content/empty-newsletter");

        Resource resource = context.resourceResolver().getResource("/content/empty-newsletter");
        FooterNewsletterModel emptyModel = resource.adaptTo(FooterNewsletterModel.class);

        assertNotNull(emptyModel);

        assertNull(emptyModel.getTitle());
        assertNull(emptyModel.getDescription());
        assertNull(emptyModel.getSocialLinks());
    }
}