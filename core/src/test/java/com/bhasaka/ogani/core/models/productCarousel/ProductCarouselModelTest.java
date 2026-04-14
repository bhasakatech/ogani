package com.bhasaka.ogani.core.models.productCarousel;

import com.bhasaka.ogani.core.services.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.sling.api.resource.ResourceResolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCarouselModelTest {

    @InjectMocks
    private ProductCarouselModel model;

    @Mock
    private ProductService productService;

    @Mock
    private ResourceResolver resolver;

    private final ObjectMapper mapper = new ObjectMapper();

    // ===============================
    // ✅ 1. INIT SUCCESS (JSON BASED)
    // ===============================
    @Test
    void testInit_Success() throws Exception {

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("product-carousel.json");

        JsonNode root = mapper.readTree(is);

        List<Product> products = new ArrayList<>();

        for (JsonNode node : root.get("products")) {
            Product p = new Product();
            p.setTitle(node.get("title").asText());
            p.setCategory(node.get("category").asText());
            p.setImage(node.get("image").asText());
            p.setOriginalPrice(node.get("originalPrice").asDouble());
            p.setCurrentPrice(node.get("currentPrice").asDouble());
            p.setDiscount(node.get("discount").asDouble());
            products.add(p);
        }

        // inject rootPath manually (since no Sling context)
        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts("/content/products", resolver))
                .thenReturn(products);

        // call init manually
        model.init();

        assertNotNull(model.getProductList());
        assertEquals(2, model.getProductList().size());
        assertFalse(model.isEmpty());
    }

    // ===============================
    // ✅ 2. INIT EXCEPTION FLOW
    // ===============================
    @Test
    void testInit_Exception() {

        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts(anyString(), any()))
                .thenThrow(new RuntimeException());

        model.init();

        assertNull(model.getProductList());
        assertTrue(model.isEmpty());
    }

    // ===============================
    // ✅ 3. EMPTY LIST CASE
    // ===============================
    @Test
    void testEmptyList() {

        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts(anyString(), any()))
                .thenReturn(new ArrayList<>());

        model.init();

        assertTrue(model.isEmpty());
    }

    // ===============================
    // ✅ 4. NULL LIST CASE
    // ===============================
    @Test
    void testNullList() {

        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts(anyString(), any()))
                .thenReturn(null);

        model.init();

        assertTrue(model.isEmpty());
    }

    // ===============================
    // 🔧 Reflection Helper
    // ===============================
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}