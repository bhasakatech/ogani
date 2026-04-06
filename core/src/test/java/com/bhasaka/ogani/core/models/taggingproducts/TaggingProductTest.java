package com.bhasaka.ogani.core.models.taggingproducts;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class TaggingProductTest {

    @Test
    void testAllValidValues() {
        List<String> categories = Arrays.asList("fruit", "fresh");

        TaggingProduct product = new TaggingProduct(
                "Apple",
                "100",
                "/img/apple.png",
                categories
        );

        assertEquals("Apple", product.getTitle());
        assertEquals("100", product.getPrice());
        assertEquals("/img/apple.png", product.getImage());
        assertEquals(categories, product.getCategories());
    }

    @Test
    void testAllNullValues() {
        TaggingProduct product = new TaggingProduct(
                null, null, null, null
        );

        assertEquals("", product.getTitle());
        assertEquals("", product.getPrice());
        assertEquals("", product.getImage());
        assertNotNull(product.getCategories());
        assertTrue(product.getCategories().isEmpty());
    }

    @Test
    void testMixedValues() {
        TaggingProduct product = new TaggingProduct(
                "Banana",     // non-null
                null,         // null
                "/img/banana.png", // non-null
                null          // null
        );

        assertEquals("Banana", product.getTitle());        // true branch
        assertEquals("", product.getPrice());              // false branch
        assertEquals("/img/banana.png", product.getImage());// true branch
        assertTrue(product.getCategories().isEmpty());     // false branch
    }

    @Test
    void testEmptyCategoriesNotNull() {
        TaggingProduct product = new TaggingProduct(
                "Orange",
                "50",
                "/img/orange.png",
                Collections.emptyList()
        );

        assertNotNull(product.getCategories());
        assertTrue(product.getCategories().isEmpty());
    }

    @Test
    void testCategoriesReference() {
        List<String> categories = Arrays.asList("veg");

        TaggingProduct product = new TaggingProduct(
                "Carrot",
                "30",
                "/img/carrot.png",
                categories
        );

        assertSame(categories, product.getCategories());
    }
}
