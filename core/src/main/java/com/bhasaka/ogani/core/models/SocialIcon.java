package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
/**
 * Sling model representing a social media icon item with its target link.
 */
public class SocialIcon {

    @ValueMapValue
    private String icon;

    @ValueMapValue
    private String link;

    /**
     * Returns the icon class or identifier configured for the social icon.
     *
     * @return the icon value
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns the destination URL for the social icon.
     *
     * @return the link value
     */
    public String getLink() {
        return link;
    }
}