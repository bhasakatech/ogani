package com.bhasaka.ogani.core.models.cartpage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CpProductJsonTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() throws Exception {

        context.addModelsForClasses(CpProduct.class);

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("cp-product.json");

        JsonNode root = mapper.readTree(is);
        JsonNode p = root.get("product");

        context.currentResource(
                context.create().resource("/content/product",
                        "title", p.get("title").asText(),
                        "category", p.get("category").asText(),
                        "image", p.get("image").asText(),
                        "originalPrice", p.get("originalPrice").asDouble(),
                        "currentPrice", p.get("currentPrice").asDouble(),
                        "discount", p.get("discount").asDouble()
                )
        );
    }

    @Test
    void testFullJsonData() {

        CpProduct model =
                context.currentResource().adaptTo(CpProduct.class);

        assertNotNull(model);
        assertEquals("Apple", model.getTitle());
        assertEquals("Fruits", model.getCategory());
        assertEquals("/content/dam/apple.png", model.getImage());
        assertEquals(100.0, model.getOriginalPrice());
        assertEquals(80.0, model.getCurrentPrice());
        assertEquals(20.0, model.getDiscount());
    }

    @Test
    void testMissingJsonFields() throws Exception {

        context.addModelsForClasses(CpProduct.class);

        context.currentResource(
                context.create().resource("/content/product2")
        );

        CpProduct model =
                context.currentResource().adaptTo(CpProduct.class);

        assertNotNull(model);
        assertNull(model.getTitle());
        assertNull(model.getCategory());
        assertNull(model.getImage());
        assertEquals(0.0, model.getOriginalPrice());
        assertEquals(0.0, model.getCurrentPrice());
        assertEquals(0.0, model.getDiscount());
    }

    @Test
    void testPartialJsonData() throws Exception {

        context.addModelsForClasses(CpProduct.class);

        context.currentResource(
                context.create().resource("/content/product3",
                        "title", "Milk",
                        "originalPrice", 50.0
                )
        );

        CpProduct model =
                context.currentResource().adaptTo(CpProduct.class);

        assertNotNull(model);
        assertEquals("Milk", model.getTitle());
        assertNull(model.getCategory());
        assertNull(model.getImage());
        assertEquals(50.0, model.getOriginalPrice());
        assertEquals(0.0, model.getCurrentPrice());
        assertEquals(0.0, model.getDiscount());
    }
}