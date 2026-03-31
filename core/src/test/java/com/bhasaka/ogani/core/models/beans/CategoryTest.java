package com.bhasaka.ogani.core.models.beans;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static junitx.framework.Assert.assertEquals;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CategoryTest {

    @Test
    void testConstructorAndGetters() {

        Category category = new Category("fresh-meat", "Fresh Meat");
        assertEquals("fresh-meat", category.getTag());
        assertEquals("Fresh Meat", category.getName());
    }
}