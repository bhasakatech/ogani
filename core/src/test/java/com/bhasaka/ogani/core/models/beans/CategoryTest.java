package com.bhasaka.ogani.core.models.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Category}.
 * <p>
 * This class verifies constructor initialization and getter methods,
 * including handling of null, empty, and different values.
 */
class CategoryTest {

    /**
     * Validates that constructor initializes fields correctly
     * and getters return expected values.
     */
    @Test
    void testConstructorAndGetters() {
        String tag = "electronics";
        String name = "Electronics";

        Category category = new Category(tag, name);

        assertNotNull(category);
        assertEquals(tag, category.getTag());
        assertEquals(name, category.getName());
    }

    /**
     * Verifies that the object correctly handles null values.
     */
    @Test
    void testNullValues() {
        Category category = new Category(null, null);

        assertNotNull(category);
        assertNull(category.getTag());
        assertNull(category.getName());
    }

    /**
     * Verifies that the object correctly handles empty string values.
     */
    @Test
    void testEmptyValues() {
        Category category = new Category("", "");

        assertNotNull(category);
        assertEquals("", category.getTag());
        assertEquals("", category.getName());
    }

    /**
     * Validates behavior with different input values.
     */
    @Test
    void testDifferentValues() {
        Category category = new Category("food", "Food Items");

        assertEquals("food", category.getTag());
        assertEquals("Food Items", category.getName());
    }
}