package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ProductModelTest {

    private final AemContext context = new AemContext();

    private ProductModel model;

    @BeforeEach
    void setUp() {

        context.create().resource("/content/component",
                "text", "Latest Products",
                "fragmentFolder", "/content/dam/products");

        context.create().resource("/content/dam/products");

        context.create().resource(
                "/content/dam/products/product1/jcr:content/data/master",
                "image", "/content/dam/images/spinach.png",
                "title", "Spinach",
                "price", "30"
        );

        context.create().resource(
                "/content/dam/products/product2/jcr:content/data/master",
                "image", "/content/dam/images/capsicum.png",
                "title", "Capsicum",
                "price", "40"
        );

        context.create().resource(
                "/content/dam/products/product3/jcr:content/data/master",
                "image", "/content/dam/images/carrot.png",
                "title", "Carrot",
                "price", "25"
        );

        context.currentResource("/content/component");

        
        model = context.currentResource().adaptTo(ProductModel.class);
    }

    @Test
    void testSectionTitle() {
        assertEquals("Latest Products", model.getText());
    }

    @Test
    void testProductsLoaded() {
        assertNotNull(model.getProducts());
        assertEquals(3, model.getProducts().size());
    }

    @Test
    void testFirstProductData() {
        ProductModel.Product product = model.getProducts().get(0);

        assertEquals("Spinach", product.getTitle());
        assertEquals("30", product.getPrice());
        assertEquals("/content/dam/images/spinach.png", product.getImage());
    }

    @Test
    void testSlides() {
        List<List<ProductModel.Product>> slides = model.getSlides();

        assertNotNull(slides);
        assertEquals(1, slides.size());
        assertEquals(3, slides.get(0).size());
    }

    @Test
    void testMultipleSlides() {

        context.create().resource(
                "/content/dam/products/product4/jcr:content/data/master",
                "image", "/content/dam/images/onion.png",
                "title", "Onion",
                "price", "20"
        );

        context.create().resource(
                "/content/dam/products/product5/jcr:content/data/master",
                "image", "/content/dam/images/potato.png",
                "title", "Potato",
                "price", "15"
        );

        context.currentResource("/content/component");

        
        ProductModel updatedModel =
                context.currentResource().adaptTo(ProductModel.class);

        List<List<ProductModel.Product>> slides = updatedModel.getSlides();

        assertEquals(2, slides.size());
        assertEquals(3, slides.get(0).size());
        assertEquals(2, slides.get(1).size());
    }

    @Test
    void testEmptyFolder() {

        context.create().resource("/content/emptyComponent",
                "text", "Empty Products",
                "fragmentFolder", "");

        context.currentResource("/content/emptyComponent");

        
        ProductModel emptyModel =
                context.currentResource().adaptTo(ProductModel.class);

        assertTrue(emptyModel.getProducts().isEmpty());
        assertTrue(emptyModel.getSlides().isEmpty());
    }
}