package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BlogListingModel}.
 * <p>
 * This class verifies model initialization, blog list loading behavior,
 * and property retrieval such as blog title using AEM Mocks.
 */
@ExtendWith(AemContextExtension.class)
class BlogListingModelTest {

    private final AemContext context = new AemContext();
    private static final String ROOT = "/content/bloglisting";

    /**
     * Sets up test content structure including blog parent page,
     * child pages, and component resource under test.
     */
    @BeforeEach
    void setUp() {

        // Component properties (IMPORTANT FIX)
        context.create().resource("/content/bloglisting/blogParent",
                "blogParentPath", "/content/bloglisting/blogParent",
                "blogTitle", "Latest Blogs"
        );

        // Pages
        context.create().page("/content/bloglisting/blogParent");
        context.create().page("/content/bloglisting/blogParent/child1");
        context.create().page("/content/bloglisting/blogParent/child2");
        context.create().page("/content/bloglisting/blogParent/child3");

        // Resource under test
        context.currentResource("/content/bloglisting/blogParent");
    }

    /**
     * Verifies that the model is successfully adapted from the resource.
     */
    @Test
    void testModelLoads() {

        BlogListingModel model =
                context.currentResource().adaptTo(BlogListingModel.class);

        assertNotNull(model);
    }

    /**
     * Verifies that the blogs list is initialized.
     */
    @Test
    void testBlogsSize() {

        BlogListingModel model =
                context.currentResource().adaptTo(BlogListingModel.class);

        assertEquals(0, model.getBlogs().size());
    }

    /**
     * Validates that the blog title is correctly read from component properties.
     */
    @Test
    void testTitle() {

        BlogListingModel model =
                context.currentResource().adaptTo(BlogListingModel.class);

        assertEquals("Latest Blogs", model.getBlogTitle());
    }
}