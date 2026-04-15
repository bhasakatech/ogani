package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HeaderTest {

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() throws Exception {
        ctx.load().json("/headerTest.json", "/content");

        ctx.create().tag("/content/cq:tags/departments");
        ctx.create().tag("/content/cq:tags/departments/meat");
        ctx.create().tag("/content/cq:tags/empty");

        HeaderTopBar topBar = new HeaderTopBar();
        setField(topBar, "email", "support@bhasaka.com");
        setField(topBar, "loginLink", "/login");
        setField(topBar, "loginIcon", "icon");
        setField(topBar, "promotionalText", "Promo");
        setField(topBar, "languages", new String[]{"en"});
        setField(topBar, "socialIcons", new ArrayList<>());

        ctx.registerAdapter(Resource.class, HeaderTopBar.class, topBar);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testHeaderWithValidData() {
        Resource headerResource = ctx.resourceResolver().getResource("/content/parent/header");
        Header header = headerResource.adaptTo(Header.class);

        assertNotNull(header);
        assertEquals("123-456-7890", header.getPhoneNumber());
        assertEquals("24/7 Support", header.getSupportText());
        assertTrue(header.isEnableCart());
        assertNotNull(header.getTopHeader());
        assertEquals("support@bhasaka.com", header.getEmail());
        assertEquals("/login", header.getLoginLink());
        assertEquals("icon", header.getLoginIcon());
        assertEquals("Promo", header.getPromotionalText());
        assertEquals(1, header.getLanguages().length);
        assertNotNull(header.getSocialIcons());
        assertFalse(header.getDepartmentTags().isEmpty());
        assertEquals("meat", header.getDepartmentTags().get(0));
    }

    @Test
    void testPhoneNumberFallback() {
        Header header = new Header();
        assertEquals("+65 11.188.888", header.getPhoneNumber());
    }

    @Test
    void testInitResourceIsNull() {
        Header header = new Header();
        header.init();
        assertNull(header.getTopHeader());
    }

    @Test
    void testInitParentResourceIsNull() throws Exception {
        Header header = new Header();
        Resource res = ctx.create().resource("/orphan");
        setField(header, "resource", res);
        header.init();
        assertNull(header.getTopHeader());
    }

    @Test
    void testInitTopBarResourceIsNull() {
        Resource headerResource = ctx.resourceResolver().getResource("/content/noTopBarParent/header");
        Header header = headerResource.adaptTo(Header.class);

        assertNotNull(header);
        assertNull(header.getTopHeader());
    }

    @Test
    void testInitTagRootPathBlank() {
        Resource headerResource = ctx.resourceResolver().getResource("/content/blankTagParent/header");
        Header header = headerResource.adaptTo(Header.class);

        assertNotNull(header);
        assertTrue(header.getDepartmentTags().isEmpty());
    }

    @Test
    void testInitRootTagNull() {
        Resource headerResource = ctx.resourceResolver().getResource("/content/invalidTagParent/header");
        Header header = headerResource.adaptTo(Header.class);

        assertNotNull(header);
        assertTrue(header.getDepartmentTags().isEmpty());
    }

    @Test
    void testInitNoChildTags() {
        Resource headerResource = ctx.resourceResolver().getResource("/content/emptyTagParent/header");
        Header header = headerResource.adaptTo(Header.class);

        assertNotNull(header);
        assertTrue(header.getDepartmentTags().isEmpty());
    }

    @Test
    void testInitResourceResolverNull() throws Exception {
        Header header = new Header();
        setField(header, "tagRootPath", "/content/cq:tags/departments");
        header.init();
        assertTrue(header.getDepartmentTags().isEmpty());
    }
}