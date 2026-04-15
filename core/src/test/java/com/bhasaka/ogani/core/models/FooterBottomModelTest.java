package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
/**
 * Unit test class for FooterBottomModel.
 *
 * This class verifies the behavior of FooterBottomModel using AEM Mock context.
 *
 * Coverage includes:
 * - Adaptation of Resource to FooterBottomModel
 * - Getter methods for all fields
 * - Validation of multiple fields together
 * - Handling of missing properties and default values
 *
 * Testing Approach:
 * - Uses AemContext to simulate Sling and JCR environment
 * - Creates mock resources with and without properties
 * - Adapts resources to FooterBottomModel
 * - Validates output using assertions
 *
 * Goal:
 * Ensure all fields are correctly injected and handled,
 * including edge cases where properties are missing.
 */
@ExtendWith(AemContextExtension.class)
class FooterBottomModelTest {

    /**
     * AEM context used to mock Sling environment and resources.
     */
    private final AemContext context = new AemContext();

    /**
     * Model instance under test.
     */
    private FooterBottomModel model;

    /**
     * Initializes test data before each test.
     *
     * Registers the model and creates a resource with
     * all required properties for testing.
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(FooterBottomModel.class);

        context.create().resource("/content/footer",
                "copyright", "©2026 All rights reserved",
                "paymentImage", "/content/dam/payment.png",
                "paymentAlt", "Payment methods"
        );

        Resource resource = context.resourceResolver().getResource("/content/footer");
        model = resource.adaptTo(FooterBottomModel.class);
    }

    /**
     * Verifies that the model is successfully adapted from resource.
     */
    @Test
    void testModelNotNull() {
        assertNotNull(model);
    }

    /**
     * Tests retrieval of copyright value.
     *
     * Verifies that the value is correctly injected from resource.
     */
    @Test
    void testCopyright() {
        assertEquals("©2026 All rights reserved", model.getCopyright());
    }

    /**
     * Tests retrieval of payment image path.
     *
     * Verifies that the image path is correctly injected.
     */
    @Test
    void testPaymentImage() {
        assertEquals("/content/dam/payment.png", model.getPaymentImage());
    }

    /**
     * Tests retrieval of payment image alt text.
     *
     * Verifies that alt text is correctly injected.
     */
    @Test
    void testPaymentAlt() {
        assertEquals("Payment methods", model.getPaymentAlt());
    }

    /**
     * Tests all fields together using grouped assertions.
     *
     * Verifies that all properties are correctly populated at once.
     */
    @Test
    void testAllFieldsTogether() {
        assertAll(
                () -> assertEquals("©2026 All rights reserved", model.getCopyright()),
                () -> assertEquals("/content/dam/payment.png", model.getPaymentImage()),
                () -> assertEquals("Payment methods", model.getPaymentAlt())
        );
    }

    /**
     * Tests behavior when properties are missing.
     *
     * Verifies that:
     * - Model is still created successfully
     * - All fields return null when not present in resource
     */
    @Test
    void testEmptyValues() {

        context.create().resource("/content/empty");

        FooterBottomModel emptyModel =
                context.resourceResolver()
                        .getResource("/content/empty")
                        .adaptTo(FooterBottomModel.class);

        assertNotNull(emptyModel);
        assertNull(emptyModel.getCopyright());
        assertNull(emptyModel.getPaymentImage());
        assertNull(emptyModel.getPaymentAlt());
    }
}