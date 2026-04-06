package com.bhasaka.ogani.core.models.beans;

import static org.junit.jupiter.api.Assertions.*;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class BlogPageItemTest {

    @Test
    void testConstructorAndGetters() {

        Date date = new Date();

        BlogPageItem item = new BlogPageItem(
                "Title",
                "Description",
                "/content/dam/image.jpg",
                "/content/blog.html",
                "May 04, 2025",
                date
        );

        assertEquals("Title", item.getTitle());
        assertEquals("Description", item.getDescription());
        assertEquals("/content/dam/image.jpg", item.getImage());
        assertEquals("/content/blog.html", item.getPath());
        assertEquals("May 04, 2025", item.getPublishDate());
        assertEquals(date, item.getDate());
    }

    @Test
    void testWithNullValues() {

        BlogPageItem item = new BlogPageItem(
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

    @Test
    void testWithEmptyValues() {

        Date date = new Date();

        BlogPageItem item = new BlogPageItem(
                "",
                "",
                "",
                "",
                "",
                date
        );

        assertEquals("", item.getTitle());
        assertEquals("", item.getDescription());
        assertEquals("", item.getImage());
        assertEquals("", item.getPath());
        assertEquals("", item.getPublishDate());
        assertEquals(date, item.getDate());
    }

    @Test
    void testWithDifferentDates() {

        Date pastDate = new Date(0); // Epoch
        Date futureDate = new Date(System.currentTimeMillis() + 100000);

        BlogPageItem item1 = new BlogPageItem("T1", "D1", "I1", "P1", "Date1", pastDate);
        BlogPageItem item2 = new BlogPageItem("T2", "D2", "I2", "P2", "Date2", futureDate);

        assertEquals(pastDate, item1.getDate());
        assertEquals(futureDate, item2.getDate());
    }

    @Test
    void testObjectNotNull() {

        BlogPageItem item = new BlogPageItem("T", "D", "I", "P", "Date", new Date());

        assertNotNull(item);
    }
}