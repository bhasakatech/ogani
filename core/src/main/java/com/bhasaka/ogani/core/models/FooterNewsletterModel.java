package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = FooterNewsletterModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterNewsletterModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ChildResource(name = "socialLinks")
    private List<SocialLink> socialLinks;

    // ================= GETTERS =================

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<SocialLink> getSocialLinks() {
        return socialLinks;
    }

    // ================= INNER CLASS =================
    @Model(adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class SocialLink {

        @ValueMapValue
        private String url;

        @ValueMapValue
        private String iconClass;

        public String getIconClass() {
            return iconClass;
        }

        public String getUrl() {
            return url;
        }
    }
}