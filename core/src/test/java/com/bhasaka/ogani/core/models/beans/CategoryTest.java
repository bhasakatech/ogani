package com.bhasaka.ogani.core.models.beans;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testConstructorAndGetters() {
        String tag = "electronics";
        String name = "Electronics";

        Category category = new Category(tag, name);

        assertNotNull(category);
        assertEquals(tag, category.getTag());
        assertEquals(name, category.getName());
    }

    @Test
    void testNullValues() {
        Category category = new Category(null, null);

        assertNotNull(category);
        assertNull(category.getTag());
        assertNull(category.getName());
    }

    @Test
    void testEmptyValues() {
        Category category = new Category("", "");

        assertNotNull(category);
        assertEquals("", category.getTag());
        assertEquals("", category.getName());
    }

    @Test
    void testDifferentValues() {
        Category category = new Category("food", "Food Items");

        assertEquals("food", category.getTag());
        assertEquals("Food Items", category.getName());
    }
}