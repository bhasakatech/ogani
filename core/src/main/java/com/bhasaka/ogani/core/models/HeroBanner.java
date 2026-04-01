package com.bhasaka.ogani.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroBanner {

    @ValueMapValue
    private String backgroundImage;

    @ValueMapValue
    private String bannerTitle;

    @ValueMapValue
    private Boolean overlay;

    @ScriptVariable
    private Page currentPage;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public Boolean getOverlay() {
        return overlay != null && overlay;
    }
}