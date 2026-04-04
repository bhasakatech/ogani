package com.bhasaka.ogani.core.models.beans;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CategoryTest {

    @Test
    void testConstructorAndGetters() {

        Category category = new Category("fresh-meat", "Fresh Meat");
        assertEquals("fresh-meat", category.getTag());
        assertEquals("Fresh Meat", category.getName());
    }
}