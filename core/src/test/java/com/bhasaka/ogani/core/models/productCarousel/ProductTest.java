package com.bhasaka.ogani.core.models.productCarousel;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductTest {

    private final AemContext context = new AemContext();

    private Product product;

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(Product.class);

        context.create().resource("/content/product",
                "title", "Apple",
                "category", "Fruits",
                "image", "/content/dam/images/apple.png",
                "originalPrice", 100.0,
                "currentPrice", 80.0,
                "discount", 20.0
        );

        Resource resource = context.resourceResolver().getResource("/content/product");
        product = resource.adaptTo(Product.class);
    }

    //BASIC MODEL TEST
    @Test
    void testModel_NotNull() {
        assertNotNull(product);
    }

    //GETTERS FROM RESOURCE
    @Test
    void testGetters_FromResource() {

        assertEquals("Apple", product.getTitle());
        assertEquals("Fruits", product.getCategory());
        assertEquals("/content/dam/images/apple.png", product.getImage());

        assertEquals(100.0, product.getOriginalPrice());
        assertEquals(80.0, product.getCurrentPrice());
        assertEquals(20.0, product.getDiscount());
    }

    //SETTERS NORMAL FLOW
    @Test
    void testSetters() {

        product.setTitle("Mango");
        product.setCategory("Tropical");
        product.setImage("/content/dam/images/mango.png");
        product.setOriginalPrice(200.0);
        product.setCurrentPrice(150.0);
        product.setDiscount(25);

        assertEquals("Mango", product.getTitle());
        assertEquals("Tropical", product.getCategory());
        assertEquals("/content/dam/images/mango.png", product.getImage());

        assertEquals(200.0, product.getOriginalPrice());
        assertEquals(150.0, product.getCurrentPrice());
        assertEquals(25.0, product.getDiscount());
    }

    @Test
    void testDefaultValues_WhenPropertiesMissing() {

        context.create().resource("/content/emptyProduct");

        Resource resource = context.resourceResolver().getResource("/content/emptyProduct");
        Product emptyProduct = resource.adaptTo(Product.class);

        assertNotNull(emptyProduct);

        assertNull(emptyProduct.getTitle());
        assertNull(emptyProduct.getCategory());
        assertNull(emptyProduct.getImage());

        assertEquals(0.0, emptyProduct.getOriginalPrice());
        assertEquals(0.0, emptyProduct.getCurrentPrice());
        assertEquals(0.0, emptyProduct.getDiscount());
    }

    @Test
    void testPartialValues() {

        context.create().resource("/content/partialProduct",
                "title", "Banana",
                "originalPrice", 50.0
        );

        Resource resource = context.resourceResolver().getResource("/content/partialProduct");
        Product partialProduct = resource.adaptTo(Product.class);

        assertEquals("Banana", partialProduct.getTitle());
        assertNull(partialProduct.getCategory());

        assertEquals(50.0, partialProduct.getOriginalPrice());
        assertEquals(0.0, partialProduct.getCurrentPrice());
        assertEquals(0.0, partialProduct.getDiscount());
    }

    @Test
    void testSetters_NullAndEmptyValues() {

        product.setTitle(null);
        product.setCategory("");
        product.setImage(null);

        assertNull(product.getTitle());
        assertEquals("", product.getCategory());
        assertNull(product.getImage());
    }

    @Test
    void testSetters_NegativeAndZeroValues() {

        product.setOriginalPrice(-100.0);
        product.setCurrentPrice(0.0);
        product.setDiscount(-10.0);

        assertEquals(-100.0, product.getOriginalPrice());
        assertEquals(0.0, product.getCurrentPrice());
        assertEquals(-10.0, product.getDiscount());
    }

    @Test
    void testSetters_OverwriteValues() {

        product.setTitle("Apple");
        product.setTitle("Orange");

        assertEquals("Orange", product.getTitle());
    }

    @Test
    void testAdaptTo_NullResource() {

        Resource resource = context.resourceResolver().getResource("/invalid/path");

        Product result = null;
        if (resource != null) {
            result = resource.adaptTo(Product.class);
        }

        assertNull(result);
    }
}