package com.bhasaka.ogani.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class BlogDetailModelTest {

    private final AemContext context = new AemContext();
    private BlogDetailModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(BlogDetailModel.class);
        context.load().json("/blog-detail.json", "/content/ogani/blog-detail");
        context.create().resource("/content/ogani/blog-detail/jcr:content/root/blog-detail",
                "blogDetailPath", "/content/ogani/blog-detail",     // Must point to page
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

    @Test
    void modelShouldLoad() {
        assertNotNull(model);
    }

    @Test
    void debugPageLoading() {
        String path = model.getBlogDetailPath();
        assertNotNull(path, "blogDetailPath should not be null");

        Resource page = context.resourceResolver().getResource(path);
        assertNotNull(page, "Page resource not found at: " + path);

        Resource content = page.getChild("jcr:content");
        assertNotNull(content, "jcr:content node not found under page");
    }

    @Test
    void testTitle() {
        assertNotNull(model.getTitle(), "Title is null");
        assertEquals("", model.getTitle());
    }

}