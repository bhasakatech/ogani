package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FooterBottomModelTest {

    private final AemContext context = new AemContext();

    private FooterBottomModel model;


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

    @Test
    void testModelNotNull() {
        assertNotNull(model);
    }

    @Test
    void testCopyright() {
        assertEquals("©2026 All rights reserved", model.getCopyright());
    }

    @Test
    void testPaymentImage() {
        assertEquals("/content/dam/payment.png", model.getPaymentImage());
    }

    @Test
    void testPaymentAlt() {
        assertEquals("Payment methods", model.getPaymentAlt());
    }

    @Test
    void testAllFieldsTogether() {
        assertAll(
                () -> assertEquals("©2026 All rights reserved", model.getCopyright()),
                () -> assertEquals("/content/dam/payment.png", model.getPaymentImage()),
                () -> assertEquals("Payment methods", model.getPaymentAlt())
        );
    }

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