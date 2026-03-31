package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = FooterBottomModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterBottomModel {

    @ValueMapValue
    private String copyright;


    @ValueMapValue(name = "paymentImage")
    private String paymentImage;

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