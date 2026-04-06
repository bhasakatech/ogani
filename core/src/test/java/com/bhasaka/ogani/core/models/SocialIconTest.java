package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class SocialIconTest {

    public final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(SocialIcon.class);
        ctx.load().json("/socialIconTest.json", "/content/socialicons");
    }

    @Test
    void testGetters_HappyPath() {
        Resource resource = ctx.resourceResolver().getResource("/content/socialicons/happy-path");
        assertNotNull(resource);

        SocialIcon socialIcon = resource.adaptTo(SocialIcon.class);

        assertNotNull(socialIcon);
        assertEquals("fa-facebook", socialIcon.getIcon());
        assertEquals("https://facebook.com/ogani", socialIcon.getLink());
    }

    @Test
    void testAdaptation_WhenIconIsMissing() {
        Resource resource = ctx.resourceResolver().getResource("/content/socialicons/missing-icon-path");
        assertNotNull(resource);

        SocialIcon socialIcon = resource.adaptTo(SocialIcon.class);

        assertNotNull(socialIcon);
        assertNull(socialIcon.getIcon());
        assertEquals("https://twitter.com/ogani", socialIcon.getLink());
    }

    @Test
    void testAdaptation_WhenLinkIsMissing() {
        Resource resource = ctx.resourceResolver().getResource("/content/socialicons/missing-link-path");
        assertNotNull(resource);

        SocialIcon socialIcon = resource.adaptTo(SocialIcon.class);

        assertNotNull(socialIcon);
        assertNull(socialIcon.getLink());
        assertEquals("fa-twitter", socialIcon.getIcon());
    }
}