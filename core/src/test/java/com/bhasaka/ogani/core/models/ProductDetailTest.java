package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.Product;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductDetailTest {

    private final AemContext context = new AemContext();

    @Test
    void testProductsPopulatedFromCF() {
        // Simulate CF master data node under your path
        context.create().resource("/content/dam/Ogani/productdetailscf/product1/jcr:content/data/master",
                "title", "Test Product",
                "price", "99.99",
                "image", "/content/dam/Ogani/product1.jpg",
                "description", "Sample description",
                "category", new String[]{"Category1"});

        // Create component resource with cfPath property pointing to your CF path
        Resource resource = context.create().resource("/content/component",
                "cfPath", "/content/dam/Ogani/productdetailscf",
                "availability", "In Stock",
                "shipping", "Free Shipping",
                "weight", "1kg",
                "facebook", "http://facebook.com/test",
                "twitter", "http://twitter.com/test",
                "instagram", "http://instagram.com/test",
                "pinterest", "http://pinterest.com/test",
                "prdDesc", "Description text",
                "prdInfo", "Information text",
                "prdReviews", "Reviews text");

        // Adapt request to model
        context.currentResource(resource);
        ProductDetail model = context.request().adaptTo(ProductDetail.class);

        // Verify product list
        List<Product> products = model.getProducts();
        assertEquals(1, products.size());
        Product p = products.get(0);
        assertEquals("Test Product", p.getTitle());
        assertEquals("99.99", p.getPrice());
        assertEquals("/content/dam/Ogani/product1.jpg", p.getImage());
        assertEquals("Sample description", p.getDescription());
        assertArrayEquals(new String[]{"Category1"}, p.getCategory());

        // Verify meta fields
        assertEquals("In Stock", model.getAvailability());
        assertEquals("Free Shipping", model.getShipping());
        assertEquals("1kg", model.getWeight());

        // Verify social links
        assertEquals("http://facebook.com/test", model.getFacebook());
        assertEquals("http://twitter.com/test", model.getTwitter());
        assertEquals("http://instagram.com/test", model.getInstagram());
        assertEquals("http://pinterest.com/test", model.getPinterest());

        // Verify tab fields
        assertEquals("Description text", model.getPrdDesc());
        assertEquals("Information text", model.getPrdInfo());
        assertEquals("Reviews text", model.getPrdReviews());
    }

    @Test
    void testEmptyCfPathDoesNotPopulateProducts() {
        Resource resource = context.create().resource("/content/component",
                "cfPath", "");

        context.currentResource(resource);
        ProductDetail model = context.request().adaptTo(ProductDetail.class);

        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testNullParentResourceDoesNotPopulateProducts() {
        Resource resource = context.create().resource("/content/component",
                "cfPath", "/content/dam/missing");

        context.currentResource(resource);
        ProductDetail model = context.request().adaptTo(ProductDetail.class);

        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testMultipleProducts() {
        // Add two product CFs under your path
        context.create().resource("/content/dam/Ogani/productdetailscf/product1/jcr:content/data/master",
                "title", "Product One",
                "price", "10.00",
                "image", "/content/dam/Ogani/product1.jpg",
                "description", "Desc1",
                "category", new String[]{"Cat1"});

        context.create().resource("/content/dam/Ogani/productdetailscf/product2/jcr:content/data/master",
                "title", "Product Two",
                "price", "20.00",
                "image", "/content/dam/Ogani/product2.jpg",
                "description", "Desc2",
                "category", new String[]{"Cat2"});

        Resource resource = context.create().resource("/content/component",
                "cfPath", "/content/dam/Ogani/productdetailscf");

        context.currentResource(resource);
        ProductDetail model = context.request().adaptTo(ProductDetail.class);

        List<Product> products = model.getProducts();
        assertEquals(2, products.size());
        assertEquals("Product One", products.get(0).getTitle());
        assertEquals("Product Two", products.get(1).getTitle());
    }
}