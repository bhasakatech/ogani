package com.bhasaka.ogani.core.models.beans;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BlogItem}.
 * <p>
 * This class verifies constructor initialization, getter methods,
 * null handling, and Date reference behavior.
 */
class BlogItemTest {

    /**
     * Validates that constructor initializes all fields correctly
     * and getters return expected values.
     */
    @Test
    void testConstructorAndGetters() {

        Date now = new Date();
        BlogItem item = new BlogItem(
                "Test Title",
                "Test Description",
                "/content/dam/test.jpg",
                "/content/blog/test.html",
                "Apr 15, 2026",
                now
        );

        assertNotNull(item);
        assertEquals("Test Title", item.getTitle());
        assertEquals("Test Description", item.getDescription());
        assertEquals("/content/dam/test.jpg", item.getImage());
        assertEquals("/content/blog/test.html", item.getPath());
        assertEquals("Apr 15, 2026", item.getPublishDate());
        assertEquals(now, item.getDate());
    }

    /**
     * Verifies that the object correctly handles null values.
     */
    @Test
    void testNullValues() {

        BlogItem item = new BlogItem(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertNull(item.getTitle());
        assertNull(item.getDescription());
        assertNull(item.getImage());
        assertNull(item.getPath());
        assertNull(item.getPublishDate());
        assertNull(item.getDate());
    }

    /**
     * Verifies that the same Date reference is returned by getter.
     */
    @Test
    void testDateGetter() {

        Date date = new Date();
        BlogItem item = new BlogItem(
                "title",
                "description",
                "image",
                "path",
                "publishDate",
                date
        );

        assertSame(date, item.getDate());
    }

    /**
     * Validates that the Date object reference is not deeply cloned,
     * and external modifications affect internal state.
     */
    @Test
    void testImmutabilityBehavior() {

        Date date = new Date();
        BlogItem item = new BlogItem(
                "Title",
                "Desc",
                "Img",
                "Path",
                "DateStr",
                date
        );

        date.setTime(0);

        // BlogItem still holds reference (important behavior check)
        assertEquals(0, item.getDate().getTime());
    }
}