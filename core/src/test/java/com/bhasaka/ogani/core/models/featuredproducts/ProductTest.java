package com.bhasaka.ogani.core.models.featuredproducts;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Product}.
 * <p>
 * This class verifies the constructor and getter methods
 * of the Product POJO.
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductTest {

    /**
     * Validates that the constructor correctly initializes
     * all fields and getters return expected values.
     */
    @Test
    void testConstructorAndGetters() {

        List<String> categories = Arrays.asList("fruits", "vegetables");

        Product product = new Product(
                "Apple",
                "10",
                "/img/apple.png",
                categories
        );

        assertEquals("Apple", product.getTitle());
        assertEquals("10", product.getPrice());
        assertEquals("/img/apple.png", product.getImage());
        assertEquals(categories, product.getCategories());
    }
}