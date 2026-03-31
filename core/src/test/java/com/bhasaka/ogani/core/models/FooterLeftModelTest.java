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
class FooterLeftModelTest {

    private final AemContext context = new AemContext();

    private FooterLeftModel model;

    @BeforeEach
    void setUp() {
        // Load JSON into AEM mock context
        context.load().json("/footer-left.json", "/content/footer");

        // Register model
        context.addModelsForClasses(FooterLeftModel.class);

        // Get resource
        Resource resource = context.resourceResolver().getResource("/content/footer");

        // Adapt to model
        model = resource.adaptTo(FooterLeftModel.class);
    }

    @Test
    void testBasicFields() {
        assertNotNull(model);

        assertEquals("/content/dam/logo.png", model.getLogo());
        assertEquals("Hyderabad, India", model.getAddress());
        assertEquals("+91-9876543210", model.getPhone());
        assertEquals("test@example.com", model.getEmail());
    }

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

    @Test
    void testEmptyModel() {
        // Load empty resource
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