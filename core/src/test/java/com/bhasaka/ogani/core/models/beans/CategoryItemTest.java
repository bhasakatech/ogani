package com.bhasaka.ogani.core.models.beans;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CategoryItem}.
 * <p>
 * This class verifies constructor initialization, getter methods,
 * and handling of null, empty, negative, and large count values.
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CategoryItemTest {

    /**
     * Validates that constructor initializes all fields correctly
     * and getters return expected values.
     */
    @Test
    void testConstructorAndGetters() {

        CategoryItem item = new CategoryItem("blog:beauty", "Beauty", 20);

        assertEquals("blog:beauty", item.getTagId());
        assertEquals("Beauty", item.getTitle());
        assertEquals(20, item.getCount());
    }

    /**
     * Verifies that the object correctly handles null values.
     */
    @Test
    void testWithNullValues() {

        CategoryItem item = new CategoryItem(null, null, 0);

        assertNull(item.getTagId());
        assertNull(item.getTitle());
        assertEquals(0, item.getCount());
    }

    /**
     * Verifies that the object correctly handles empty string values.
     */
    @Test
    void testWithEmptyValues() {

        CategoryItem item = new CategoryItem("", "", 0);

        assertEquals("", item.getTagId());
        assertEquals("", item.getTitle());
        assertEquals(0, item.getCount());
    }

    /**
     * Validates behavior with negative count values.
     */
    @Test
    void testWithNegativeCount() {

        CategoryItem item = new CategoryItem("blog:test", "Test", -5);

        assertEquals("blog:test", item.getTagId());
        assertEquals("Test", item.getTitle());
        assertEquals(-5, item.getCount());
    }

    /**
     * Validates behavior with large count values.
     */
    @Test
    void testWithLargeCount() {

        CategoryItem item = new CategoryItem("blog:large", "Large", Integer.MAX_VALUE);

        assertEquals("blog:large", item.getTagId());
        assertEquals("Large", item.getTitle());
        assertEquals(Integer.MAX_VALUE, item.getCount());
    }
}