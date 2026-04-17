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
/**
 * Unit test class for ProductCarouselModel.
 *
 * This class verifies the behavior of ProductCarouselModel using Mockito.
 *
 * Coverage includes:
 * - Initialization logic in init method
 * - Interaction with ProductService
 * - Handling of valid data, empty data, null data, and exceptions
 * - Behavior of helper methods like isEmpty and getProductList
 *
 * Testing Approach:
 * - Uses Mockito to mock ProductService and ResourceResolver
 * - Uses JSON input to simulate realistic product data
 * - Uses reflection to inject private fields such as rootPath and resolver
 *
 * Goal:
 * Ensure high method and branch coverage by testing all execution paths.
 */
@ExtendWith(MockitoExtension.class)
class ProductCarouselModelTest {

    /**
     * Model instance under test.
     * InjectMocks is used to inject mocked dependencies.
     */
    @InjectMocks
    private ProductCarouselModel model;

    /**
     * Mocked ProductService used to simulate service layer behavior.
     */
    @Mock
    private ProductService productService;

    /**
     * Mocked ResourceResolver required by the model.
     */
    @Mock
    private ResourceResolver resolver;

    /**
     * ObjectMapper used to read JSON test data.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Tests successful initialization of the model.
     *
     * Verifies that:
     * - Product data is loaded correctly from JSON
     * - Service returns expected list
     * - productList is populated
     * - isEmpty returns false
     */
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

        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts("/content/products", resolver))
                .thenReturn(products);

        model.init();

        assertNotNull(model.getProductList());
        assertEquals(2, model.getProductList().size());
        assertFalse(model.isEmpty());
    }

    /**
     * Tests initialization when service throws an exception.
     *
     * Verifies that:
     * - Exception is handled internally
     * - productList remains null
     * - isEmpty returns true
     */
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

    /**
     * Tests initialization when service returns an empty list.
     *
     * Verifies that:
     * - productList is empty
     * - isEmpty returns true
     */
    @Test
    void testEmptyList() {

        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts(anyString(), any()))
                .thenReturn(new ArrayList<>());

        model.init();

        assertTrue(model.isEmpty());
    }

    /**
     * Tests initialization when service returns null.
     *
     * Verifies that:
     * - productList remains null
     * - isEmpty returns true
     */
    @Test
    void testNullList() {

        setField(model, "rootPath", "/content/products");
        setField(model, "resolver", resolver);

        when(productService.getProducts(anyString(), any()))
                .thenReturn(null);

        model.init();

        assertTrue(model.isEmpty());
    }

    /**
     * Utility method to set private fields using reflection.
     *
     * Used to inject values into private fields such as rootPath and resolver,
     * since Sling injection is not available in unit tests.
     *
     * @param target object where field is to be set
     * @param fieldName name of the field
     * @param value value to assign
     */
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