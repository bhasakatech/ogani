package com.bhasaka.ogani.core.models.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryConstructorAndGetters() {

        // ---------Arrange-------
        String title = "Fruits";
        String image = "/content/dam/ogani/fruits.png";
        String link = "/content/ogani/fruits";
        Category category = new Category(title, image, link);
        assertNotNull(category);
        assertEquals(title, category.getTitle());
        assertEquals(image, category.getImage());
        assertEquals(link, category.getLink());
    }
}
