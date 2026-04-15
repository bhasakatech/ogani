package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BlogDetailModel}.
 * <p>
 * This class verifies the proper initialization of the model,
 * resource loading, and basic getter validations using AEM Mocks.
 */
@ExtendWith(AemContextExtension.class)
class BlogDetailModelTest {

    private final AemContext context = new AemContext();
    private BlogDetailModel model;

    /**
     * Sets up the test context by loading JSON content,
     * creating component resource, and adapting the model.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(BlogDetailModel.class);
        context.load().json("/blog-detail.json", "/content/ogani/blog-detail");
        context.create().resource("/content/ogani/blog-detail/jcr:content/root/blog-detail",
                "blogDetailPath", "/content/ogani/blog-detail",
                "blogParentPath", "/content/ogani/blogs",
                "categoryRootPath", "/content/cq:tags/test",
                "recentBlogPagesLimit", "5",
                "searchPlaceholder", "Search blogs..."
        );

        Resource component = context.resourceResolver()
                .getResource("/content/ogani/blog-detail/jcr:content/root/blog-detail");

        assertNotNull(component, "Component resource not found");

        context.request().setResource(component);
        model = context.request().adaptTo(BlogDetailModel.class);
    }

    /**
     * Verifies that the model is successfully adapted.
     */
    @Test
    void modelShouldLoad() {
        assertNotNull(model);
    }

    /**
     * Validates that the configured page path resolves correctly
     * and contains the expected jcr:content node.
     */
    @Test
    void debugPageLoading() {
        String path = model.getBlogDetailPath();
        assertNotNull(path, "blogDetailPath should not be null");

        Resource page = context.resourceResolver().getResource(path);
        assertNotNull(page, "Page resource not found at: " + path);

        Resource content = page.getChild("jcr:content");
        assertNotNull(content, "jcr:content node not found under page");
    }

    /**
     * Validates the title value returned by the model.
     */
    @Test
    void testTitle() {
        assertNotNull(model.getTitle(), "Title is null");
        assertEquals("", model.getTitle());
    }
}