package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroBanner {

    @ValueMapValue
    private String backgroundImage;

    @ValueMapValue
    private Boolean overlay;

    @ScriptVariable
    private Page currentPage;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public Boolean getOverlay() {
        return overlay != null && overlay;
    }
}