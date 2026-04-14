package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for Footer Bottom section.
 *
 * This model represents the bottom part of the footer, including
 * copyright information and payment method details.
 *
 * Data Source:
 * - Reads properties from component resource
 *
 * Responsibilities:
 * - Provide copyright text
 * - Provide payment image and alt text for accessibility
 *
 * Injection Strategy:
 * Uses OPTIONAL injection to avoid errors when properties are missing.
 */
@Model(
        adaptables = Resource.class,
        adapters = FooterBottomModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterBottomModel {

    /**
     * Copyright text displayed in footer.
     */
    @ValueMapValue
    private String copyright;

    /**
     * Payment methods image path.
     */
    @ValueMapValue(name = "paymentImage")
    private String paymentImage;

    /**
     * Alt text for payment image.
     */
    @ValueMapValue(name = "paymentAlt")
    private String paymentAlt;

    public String getCopyright() {
        return copyright;
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public String getPaymentAlt() {
        return paymentAlt;
    }
}