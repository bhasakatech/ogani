package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class HeaderTest {

    public final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForPackage("com.bhasaka.ogani.core.models");
        ctx.load().json("/HeaderTest.json", "/content/ogani");
    }

    @Test
    void testInitAndGetters_HappyPath() {
        ctx.create().resource("/content/cq:tags/departments", "jcr:primaryType", "cq:Tag");
        ctx.create().resource("/content/cq:tags/departments/cardiology",
                "jcr:primaryType", "cq:Tag",
                "jcr:title", "Cardiology");

        Resource currentResource = ctx.resourceResolver().getResource("/content/ogani/parent/happy-path");
        ctx.currentResource(currentResource);
        ctx.request().setResource(currentResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header, "Model should adapt successfully.");
        assertEquals("+1 234 567 8900", header.getPhoneNumber());
        assertEquals("24/7 Customer Support", header.getSupportText());
        assertTrue(header.isEnableCart());

        assertEquals("test@test.com", header.getEmail());
        assertEquals("/content/login", header.getLoginLink());
        assertEquals("icon-user", header.getLoginIcon());
        assertEquals("Promo!", header.getPromotionalText());
        assertArrayEquals(new String[]{"en", "fr"}, header.getLanguages());
        assertEquals(1, header.getSocialIcons().size());
        assertNotNull(header.getTopHeader());

        assertFalse(header.getDepartmentTags().isEmpty());
        assertEquals("Cardiology", header.getDepartmentTags().get(0));
    }

    @Test
    void testGetPhoneNumber_FallbackPath() {
        Resource currentResource = ctx.resourceResolver().getResource("/content/ogani/parent/fallback-phone");
        ctx.currentResource(currentResource);
        ctx.request().setResource(currentResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header);
        assertEquals("+65 11.188.888", header.getPhoneNumber(), "Should fall back to default phone number");
        assertNull(header.getSupportText());
        assertFalse(header.isEnableCart());
    }

    @Test
    void testInit_BlankTagRootPath() {
        Resource currentResource = ctx.resourceResolver().getResource("/content/ogani/parent/blank-tag");
        ctx.currentResource(currentResource);
        ctx.request().setResource(currentResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header);
        assertTrue(header.getDepartmentTags().isEmpty(), "Tags list should be empty if root path is blank");
    }

    @Test
    void testInit_NullRootTag() {
        Resource currentResource = ctx.resourceResolver().getResource("/content/ogani/parent/invalid-tag");
        ctx.currentResource(currentResource);
        ctx.request().setResource(currentResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header);
        assertTrue(header.getDepartmentTags().isEmpty(), "Tags list should be empty if root tag fails to resolve");
    }

    @Test
    void testInit_MissingTopHeader() {
        Resource currentResource = ctx.resourceResolver().getResource("/content/ogani/parent-missing-topbar/current");
        ctx.currentResource(currentResource);
        ctx.request().setResource(currentResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header);
        assertNull(header.getTopHeader(), "Top header should be null");
        assertNull(header.getEmail(), "Email should be null because top header is missing");
    }

    @Test
    void testInit_EmptyTags() {
        ctx.create().resource("/content/cq:tags/departments", "jcr:primaryType", "cq:Tag");

        Resource currentResource = ctx.resourceResolver().getResource("/content/ogani/parent/happy-path");
        ctx.currentResource(currentResource);
        ctx.request().setResource(currentResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header);
        assertTrue(header.getDepartmentTags().isEmpty(), "Department tags should be empty if rootTag has no children");
    }

    @Test
    void testInit_NullParent() {
        Resource rootResource = ctx.resourceResolver().getResource("/");
        ctx.currentResource(rootResource);
        ctx.request().setResource(rootResource);

        Header header = ctx.request().adaptTo(Header.class);

        assertNotNull(header);
        assertNull(header.getTopHeader(), "Top header should be null because parent resource is null");
    }

    @Test
    void testInit_NullCurrentResource() {
        Header header = new Header();
        header.init();

        assertNull(header.getTopHeader(), "Top header should be null because currentResource is null");
        assertTrue(header.getDepartmentTags().isEmpty(), "Department tags should be empty");
    }

    @Test
    void testInit_NullTagManager() throws Exception {
        Header header = new Header();

       ResourceResolver proxyResolver = (ResourceResolver) java.lang.reflect.Proxy.newProxyInstance(
                HeaderTest.class.getClassLoader(),
                new Class<?>[]{ResourceResolver.class},
                (proxy, method, args) -> null
        );

        java.lang.reflect.Field rrField = Header.class.getDeclaredField("resourceResolver");
        rrField.setAccessible(true);
        rrField.set(header, proxyResolver);

        java.lang.reflect.Field pathField = Header.class.getDeclaredField("tagRootPath");
        pathField.setAccessible(true);
        pathField.set(header, "/content/tags");

        header.init();

        assertTrue(header.getDepartmentTags().isEmpty(), "Tags list should be empty if TagManager is null");
    }
}