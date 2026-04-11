package com.bhasaka.ogani.core.models.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryTest {
    
    @Test
    void testConstructorAndGetters() {
        ProductCategory category = new ProductCategory(
                "Apple",
                "/content/dam/apple.png",
                "/content/apple"
        );

        assertEquals("Apple", category.getTitle());
        assertEquals("/content/dam/apple.png", category.getImage());
        assertEquals("/content/apple", category.getLink());
    }

    @Test
    void testNullValues() {
        ProductCategory category = new ProductCategory(null, null, null);

        assertNull(category.getTitle());
        assertNull(category.getImage());
        assertNull(category.getLink());
    }

    @Test
    void testEmptyValues() {
        ProductCategory category = new ProductCategory("", "", "");
        assertEquals("", category.getTitle());
        assertEquals("", category.getImage());
        assertEquals("", category.getLink());
    }
}