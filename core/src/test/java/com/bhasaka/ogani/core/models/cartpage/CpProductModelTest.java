package com.bhasaka.ogani.core.models.cartpage;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CpProductModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(CpProductModel.class, CpProduct.class);

        context.load().json("/cp-product-model.json", "/content");

        context.currentResource("/content/test");
    }

    @Test
    void testModelInitialization() {

        CpProductModel model =
                context.currentResource().adaptTo(CpProductModel.class);

        assertNotNull(model);
        assertFalse(model.isEmpty());
        assertEquals(2, model.getProductList().size());
    }

    @Test
    void testProductValues() {

        CpProductModel model =
                context.currentResource().adaptTo(CpProductModel.class);

        CpProduct product = model.getProductList().get(0);

        assertEquals("Apple", product.getTitle());
        assertEquals("Fruits", product.getCategory());
        assertEquals("/content/dam/apple.png", product.getImage());
        assertEquals(80.0, product.getCurrentPrice());
    }

    @Test
    void testLabels() {

        CpProductModel model =
                context.currentResource().adaptTo(CpProductModel.class);

        assertEquals("Products", model.getProductsLabel());
        assertEquals("Price", model.getPriceLabel());
        assertEquals("Quantity", model.getQuantityLabel());
        assertEquals("Total", model.getTotalLabel());
    }

    @Test
    void testEmptyWhenNoProducts() {

        context.create().resource("/content/empty");

        context.currentResource("/content/empty");

        CpProductModel model =
                context.currentResource().adaptTo(CpProductModel.class);

        assertNotNull(model);
        assertTrue(model.isEmpty());
    }
}