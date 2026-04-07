package com.bhasaka.ogani.core.models.beans;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CategoryItemTest {

    @Test
    void testConstructorAndGetters() {

        CategoryItem item = new CategoryItem("blog:beauty", "Beauty", 20);
        assertEquals("blog:beauty", item.getTagId());
        assertEquals("Beauty", item.getTitle());
        assertEquals(20, item.getCount());
    }

    @Test
    void testWithNullValues() {

        CategoryItem item = new CategoryItem(null, null, 0);
        assertNull(item.getTagId());
        assertNull(item.getTitle());
        assertEquals(0, item.getCount());
    }

    @Test
    void testWithEmptyValues() {

        CategoryItem item = new CategoryItem("", "", 0);
        assertEquals("", item.getTagId());
        assertEquals("", item.getTitle());
        assertEquals(0, item.getCount());
    }

    @Test
    void testWithNegativeCount() {

        CategoryItem item = new CategoryItem("blog:test", "Test", -5);
        assertEquals("blog:test", item.getTagId());
        assertEquals("Test", item.getTitle());
        assertEquals(-5, item.getCount());
    }

    @Test
    void testWithLargeCount() {

        CategoryItem item = new CategoryItem("blog:large", "Large", Integer.MAX_VALUE);
        assertEquals("blog:large", item.getTagId());
        assertEquals("Large", item.getTitle());
        assertEquals(Integer.MAX_VALUE, item.getCount());
    }
}