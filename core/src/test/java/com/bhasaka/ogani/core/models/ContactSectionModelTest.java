package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ContactSectionModelTest {

    public final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(ContactSectionModel.class, ContactItem.class);

        ctx.load().json("/contact-Section-Test.json", "/content/contact");
    }

    @Test
    void testGetters_HappyPath() {
        Resource resource = ctx.resourceResolver().getResource("/content/contact/happy-path");
        assertNotNull(resource, "Resource is null! Check JSON path.");

        ContactSectionModel model = resource.adaptTo(ContactSectionModel.class);

        assertNotNull(model, "Model should adapt successfully");
        assertNotNull(model.getContactItems(), "List should not be null");
        assertEquals(2, model.getContactItems().size(), "Should adapt exactly 2 contact items");

        ContactItem item1 = model.getContactItems().get(0);
        assertEquals("fa-phone", item1.getIcon());
        assertEquals("Phone", item1.getTitle());
        assertEquals("+1 234 567 8900", item1.getValue());

        ContactItem item2 = model.getContactItems().get(1);
        assertEquals("fa-envelope", item2.getIcon());
        assertEquals("Email", item2.getTitle());
        assertEquals("hello@colorlib.com", item2.getValue());
    }

    @Test
    void testContactSection_EmptyPath() {
        // Arrange
        Resource resource = ctx.resourceResolver().getResource("/content/contact/empty-path");
        assertNotNull(resource);

        ContactSectionModel model = resource.adaptTo(ContactSectionModel.class);

        assertNotNull(model);
        assertNotNull(model.getContactItems(), "List should not be null due to fallback logic");
        assertTrue(model.getContactItems().isEmpty(), "List should be empty");
    }

    @Test
    void testContactItem_EmptyPath() {

        Resource resource = ctx.resourceResolver().getResource("/content/contact/empty-path");
        assertNotNull(resource);

        ContactItem item = resource.adaptTo(ContactItem.class);

        assertNotNull(item, "ContactItem should adapt successfully because properties are OPTIONAL");
        assertNull(item.getIcon());
        assertNull(item.getTitle());
        assertNull(item.getValue());
    }
}