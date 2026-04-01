package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import java.util.List;

@ExtendWith(AemContextExtension.class)
class FooterNewsletterModelTest {

    private final AemContext context = new AemContext();

    private FooterNewsletterModel model;

    @BeforeEach
    void setUp() {
        // Load JSON
        context.load().json("/footer-newsletter.json", "/content/newsletter");

        // Register model
        context.addModelsForClasses(FooterNewsletterModel.class);

        // Get resource
        Resource resource = context.resourceResolver().getResource("/content/newsletter");

        // Adapt
        model = resource.adaptTo(FooterNewsletterModel.class);
    }

    @Test
    void testBasicFields() {
        assertNotNull(model);

        assertEquals("Subscribe to our Newsletter", model.getTitle());
        assertEquals("Get updates about latest products", model.getDescription());
    }

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

    @Test
    void testEmptyModel() {
        // Create empty resource
        context.create().resource("/content/empty-newsletter");

        Resource resource = context.resourceResolver().getResource("/content/empty-newsletter");
        FooterNewsletterModel emptyModel = resource.adaptTo(FooterNewsletterModel.class);

        assertNotNull(emptyModel);

        assertNull(emptyModel.getTitle());
        assertNull(emptyModel.getDescription());
        assertNull(emptyModel.getSocialLinks());
    }
}