package com.bhasaka.ogani.core.models.productCarousel;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductCarouselModelTest {

    private final AemContext context = new AemContext();
    private ProductCarouselModel model;

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(ProductCarouselModel.class);

        context.create().resource("/content/test",
                "products", new String[]{
                        "/content/dam/product1",
                        "/content/dam/product2"
                }
        );

        context.create().resource("/content/dam/product1/jcr:content/data/master",
                "title", "Apple",
                "category", "Fruits",
                "image", "/content/dam/images/apple.png",
                "originalPrice", 100,
                "currentPrice", 80
        );

        context.create().resource("/content/dam/product2/jcr:content/data/master",
                "title", "Mango",
                "category", "Fruits",
                "image", "/content/dam/images/mango.png",
                "originalPrice", 200,
                "discount", 25
        );

        Resource resource = context.resourceResolver().getResource("/content/test");
        model = resource.adaptTo(ProductCarouselModel.class);
    }

    @Test
    void testProductList_Size() {
        assertEquals(2, model.getProductList().size());
    }

    @Test
    void testCurrentPriceFlow() {
        Product p = model.getProductList().get(0);
        assertEquals(80.0, p.getCurrentPrice());
        assertEquals(20, p.getDiscount());
    }

    @Test
    void testDiscountFlow() {
        Product p = model.getProductList().get(1);
        assertEquals(150.0, p.getCurrentPrice());
        assertEquals(25, p.getDiscount());
    }

    @Test
    void testProductsNull() {

        context.create().resource("/content/nullProducts");

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/nullProducts")
                .adaptTo(ProductCarouselModel.class);

        assertTrue(m.getProductList().isEmpty());
    }

    @Test
    void testEmptyPathInsideArray() {

        context.create().resource("/content/emptyPath",
                "products", new String[]{""}
        );

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/emptyPath")
                .adaptTo(ProductCarouselModel.class);

        assertTrue(m.getProductList().isEmpty());
    }

    @Test
    void testInvalidResourcePath() {

        context.create().resource("/content/invalid",
                "products", new String[]{"/invalid/path"}
        );

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/invalid")
                .adaptTo(ProductCarouselModel.class);

        assertTrue(m.getProductList().isEmpty());
    }

    @Test
    void testNonDamImagePath() {

        context.create().resource("/content/test2",
                "products", new String[]{"/content/dam/product3"}
        );

        context.create().resource("/content/dam/product3/jcr:content/data/master",
                "image", "/some/random/path/image.png",
                "originalPrice", 100,
                "currentPrice", 50
        );

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/test2")
                .adaptTo(ProductCarouselModel.class);

        Product p = m.getProductList().get(0);

        assertNotNull(p.getImage());
    }

    @Test
    void testDiscountWithoutOriginalPrice() {

        context.create().resource("/content/test3",
                "products", new String[]{"/content/dam/product4"}
        );

        context.create().resource("/content/dam/product4/jcr:content/data/master",
                "discount", 30
        );

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/test3")
                .adaptTo(ProductCarouselModel.class);

        Product p = m.getProductList().get(0);

        assertEquals(0.0, p.getCurrentPrice());
    }

    @Test
    void testExceptionHandling() {

        context.create().resource("/content/test4",
                "products", new String[]{"/content/dam/product5"}
        );

        // Missing master node -> triggers exception path
        context.create().resource("/content/dam/product5");

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/test4")
                .adaptTo(ProductCarouselModel.class);

        assertTrue(m.getProductList().isEmpty());
    }

    @Test
    void testIsEmptyTrue() {

        context.create().resource("/content/empty");

        ProductCarouselModel m = context.resourceResolver()
                .getResource("/content/empty")
                .adaptTo(ProductCarouselModel.class);

        assertTrue(m.isEmpty());
    }
}