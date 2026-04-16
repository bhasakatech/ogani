package com.bhasaka.ogani.core.models.beans;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class})
class BlogDetailItemTest {

    @Test
    void shouldCreateBlogDetailItemWithAllValues() {
        BlogDetailItem item = new BlogDetailItem(
                "Test Blog Title",
                "/content/ogani/blogs/test-blog.html",
                "/content/dam/test-image.png",
                "Apr 14, 2026"
        );

        assertEquals("Test Blog Title", item.getTitle());
        assertEquals("/content/ogani/blogs/test-blog.html", item.getPath());
        assertEquals("/content/dam/test-image.png", item.getImage());
        assertEquals("Apr 14, 2026", item.getPublishDate());
    }

    @Test
    void shouldHandleNullValues() {
        BlogDetailItem item = new BlogDetailItem(null, null, null, null);

        assertNull(item.getTitle());
        assertNull(item.getPath());
        assertNull(item.getImage());
        assertNull(item.getPublishDate());
    }

    @Test
    void shouldHandleEmptyStrings() {
        BlogDetailItem item = new BlogDetailItem("", "", "", "");

        assertEquals("", item.getTitle());
        assertEquals("", item.getPath());
        assertEquals("", item.getImage());
        assertEquals("", item.getPublishDate());
    }

    @Test
    void gettersShouldReturnCorrectValues() {
        String title = "AEM Tutorial";
        String path = "/content/ogani/blogs/aem-tutorial.html";
        String image = "/content/dam/aem.png";
        String date = "Mar 01, 2026";

        BlogDetailItem item = new BlogDetailItem(title, path, image, date);

        assertEquals(title, item.getTitle());
        assertEquals(path, item.getPath());
        assertEquals(image, item.getImage());
        assertEquals(date, item.getPublishDate());
    }
}