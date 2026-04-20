package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ProductModelTest {

    private final AemContext context = new AemContext();

    private ProductModel model;

    @BeforeEach
    void setUp() {

        // Component authored properties
        context.create().resource("/content/component",
                "text", "Latest Products",
                "fragmentFolder", "/content/dam/products");

        // DAM folder
        context.create().resource("/content/dam/products");

        // Product resources
        context.create().resource("/content/dam/products/product1");
        context.create().resource("/content/dam/products/product2");
        context.create().resource("/content/dam/products/product3");

        // Register Content Fragment mock adapter
        mockContentFragment("Spinach", "30", "/content/dam/images/spinach.png");

        // Set current resource
        context.currentResource("/content/component");

        // Adapt to model
        model = context.currentResource().adaptTo(ProductModel.class);
    }

    private void mockContentFragment(String title,
                                     String price,
                                     String image) {

        ContentFragment cf = mock(ContentFragment.class);

        ContentElement titleElement = mock(ContentElement.class);
        when(titleElement.getContent()).thenReturn(title);

        ContentElement priceElement = mock(ContentElement.class);
        when(priceElement.getContent()).thenReturn(price);

        ContentElement imageElement = mock(ContentElement.class);
        when(imageElement.getContent()).thenReturn(image);

        when(cf.getElement("title")).thenReturn(titleElement);
        when(cf.getElement("price")).thenReturn(priceElement);
        when(cf.getElement("image")).thenReturn(imageElement);

        context.registerAdapter(Resource.class, ContentFragment.class, cf);
    }

    @Test
    void testSectionTitle() {
        assertEquals("Latest Products", model.getText());
    }

    @Test
    void testProductsLoaded() {
        assertNotNull(model.getProducts());

        // Since 3 product resources exist
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

        // 3 items → 1 slide
        assertEquals(1, slides.size());

        // 3 products in slide 1
        assertEquals(3, slides.get(0).size());
    }

    @Test
    void testMultipleSlides() {

        // Add 2 more products
        context.create().resource("/content/dam/products/product4");
        context.create().resource("/content/dam/products/product5");

        context.currentResource("/content/component");

        ProductModel updatedModel =
                context.currentResource().adaptTo(ProductModel.class);

        List<List<ProductModel.Product>> slides =
                updatedModel.getSlides();

        // 5 products => 2 slides
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
