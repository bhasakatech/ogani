package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroBannerModel {

    @ValueMapValue
    private String eyebrowText;

    @ValueMapValue
    private String heroDesc;

    @ValueMapValue
    private String subText;

    public String getEyebrowText() {
        return eyebrowText;
    }

    public String getHeroDesc() {
        return heroDesc;
    }

    public String getSubText() {
        return subText;
    }
}