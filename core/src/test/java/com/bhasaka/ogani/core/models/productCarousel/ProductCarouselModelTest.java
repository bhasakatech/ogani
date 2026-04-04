package com.bhasaka.ogani.core.models.productCarousel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductCarouselModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() throws Exception {

        context.addModelsForClasses(ProductCarouselModel.class);

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("product-carousel.json");

        JsonNode root = mapper.readTree(is);
        String rootPath = root.get("rootPath").asText();

        context.create().resource(rootPath);

        Iterator<JsonNode> products = root.get("products").elements();

        while (products.hasNext()) {
            JsonNode p = products.next();

            String productPath = rootPath + "/" + p.get("name").asText();

            context.create().resource(
                    productPath + "/jcr:content/data/master",
                    "title", p.get("title").asText(),
                    "category", p.get("category").asText(),
                    "image", p.get("image").asText(),
                    "originalPrice", p.get("originalPrice").asDouble(),
                    "currentPrice", p.get("currentPrice").asDouble(),
                    "discount", p.get("discount").asDouble()
            );
        }

        context.currentResource(
                context.create().resource("/content/test",
                        "rootPath", rootPath
                )
        );
    }

    @Test
    void testModelInitialization() {

        ProductCarouselModel model =
                context.currentResource().adaptTo(ProductCarouselModel.class);

        assertNotNull(model);
        assertFalse(model.isEmpty());
        assertEquals(2, model.getProductList().size());
    }

    @Test
    void testProductValues() {

        ProductCarouselModel model =
                context.currentResource().adaptTo(ProductCarouselModel.class);

        Product product = model.getProductList().get(0);

        assertEquals("Apple", product.getTitle());
        assertEquals("Fruits", product.getCategory());
        assertEquals("/content/dam/apple.png", product.getImage());
        assertEquals(100.0, product.getOriginalPrice());
        assertEquals(80.0, product.getCurrentPrice());
        assertEquals(20, product.getDiscount());
    }

    @Test
    void testDiscountCalculation_whenOnlyDiscountPresent() {

        ProductCarouselModel model =
                context.currentResource().adaptTo(ProductCarouselModel.class);

        Product product = model.getProductList().get(1);

        assertEquals(45.0, product.getCurrentPrice());
        assertEquals(10, product.getDiscount());
    }

    @Test
    void testEmptyWhenRootPathMissing() {

        context.currentResource(
                context.create().resource("/content/test2")
        );

        ProductCarouselModel model =
                context.currentResource().adaptTo(ProductCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.isEmpty());
    }

    @Test
    void testEmptyWhenInvalidPath() {

        context.currentResource(
                context.create().resource("/content/test3",
                        "rootPath", "/invalid/path")
        );

        ProductCarouselModel model =
                context.currentResource().adaptTo(ProductCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.isEmpty());
    }
}