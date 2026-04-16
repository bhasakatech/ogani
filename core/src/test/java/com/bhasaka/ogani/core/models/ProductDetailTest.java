package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.Product;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ProductDetail} Sling Model.
 * <p>
 * These tests validate that product data is correctly loaded from Content Fragment paths
 * and that authored properties are injected properly.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class ProductDetailTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDetailTest.class);

    private final AemContext context = new AemContext();

    /**
     * Verifies that a product is correctly populated from a Content Fragment master node.
     */
    @Test
    void testProductsPopulatedFromCF() {
        try {
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

            context.currentResource(resource);
            ProductDetail model = context.request().adaptTo(ProductDetail.class);

            List<Product> products = model.getProducts();
            assertEquals(1, products.size());
            Product p = products.get(0);
            assertEquals("Test Product", p.getTitle());
            assertEquals("99.99", p.getPrice());
            assertEquals("/content/dam/Ogani/product1.jpg", p.getImage());
            assertEquals("Sample description", p.getDescription());
            assertArrayEquals(new String[]{"Category1"}, p.getCategory());

            assertEquals("In Stock", model.getAvailability());
            assertEquals("Free Shipping", model.getShipping());
            assertEquals("1kg", model.getWeight());

            assertEquals("http://facebook.com/test", model.getFacebook());
            assertEquals("http://twitter.com/test", model.getTwitter());
            assertEquals("http://instagram.com/test", model.getInstagram());
            assertEquals("http://pinterest.com/test", model.getPinterest());

            assertEquals("Description text", model.getPrdDesc());
            assertEquals("Information text", model.getPrdInfo());
            assertEquals("Reviews text", model.getPrdReviews());

            LOG.info("testProductsPopulatedFromCF passed with product: {}", p.getTitle());
        } catch (Exception e) {
            LOG.error("Error in testProductsPopulatedFromCF", e);
            throw e;
        }
    }

    /**
     * Verifies that an empty cfPath does not populate products.
     */
    @Test
    void testEmptyCfPathDoesNotPopulateProducts() {
        try {
            Resource resource = context.create().resource("/content/component", "cfPath", "");
            context.currentResource(resource);
            ProductDetail model = context.request().adaptTo(ProductDetail.class);

            assertTrue(model.getProducts().isEmpty());
            LOG.info("testEmptyCfPathDoesNotPopulateProducts passed.");
        } catch (Exception e) {
            LOG.error("Error in testEmptyCfPathDoesNotPopulateProducts", e);
            throw e;
        }
    }

    /**
     * Verifies that a missing parent resource does not populate products.
     */
    @Test
    void testNullParentResourceDoesNotPopulateProducts() {
        try {
            Resource resource = context.create().resource("/content/component", "cfPath", "/content/dam/missing");
            context.currentResource(resource);
            ProductDetail model = context.request().adaptTo(ProductDetail.class);

            assertTrue(model.getProducts().isEmpty());
            LOG.info("testNullParentResourceDoesNotPopulateProducts passed.");
        } catch (Exception e) {
            LOG.error("Error in testNullParentResourceDoesNotPopulateProducts", e);
            throw e;
        }
    }

    /**
     * Verifies that multiple products are correctly loaded from CF children.
     */
    @Test
    void testMultipleProducts() {
        try {
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

            LOG.info("testMultipleProducts passed with {} products.", products.size());
        } catch (Exception e) {
            LOG.error("Error in testMultipleProducts", e);
            throw e;
        }
    }
}
