package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;
/**
 * Sling Model for Footer Newsletter section.
 *
 * This model represents the newsletter area in the footer, including
 * title, description, and social media links.
 *
 * Data Source:
 * - Reads properties directly from the component resource
 * - Reads child resources under "socialLinks"
 *
 * Responsibilities:
 * - Provide newsletter title and description
 * - Provide list of social media links
 *
 * Injection Strategy:
 * Uses OPTIONAL injection, so missing properties do not break rendering.
 */
@Model(
        adaptables = Resource.class,
        adapters = FooterNewsletterModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterNewsletterModel {

    /**
     * Newsletter title.
     */
    @ValueMapValue
    private String title;

    /**
     * Newsletter description text.
     */
    @ValueMapValue
    private String description;

    /**
     * List of social media links.
     */
    @ChildResource(name = "socialLinks")
    private List<SocialLink> socialLinks;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<SocialLink> getSocialLinks() {
        return socialLinks;
    }

    /**
     * Inner model representing a social media link.
     *
     * Each item corresponds to a child node under "socialLinks".
     */
    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class SocialLink {

        /**
         * URL of the social media link.
         */
        @ValueMapValue
        private String url;

        /**
         * CSS class for icon representation.
         */
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