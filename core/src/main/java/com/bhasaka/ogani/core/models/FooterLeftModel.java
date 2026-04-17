package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;
/**
 * Sling Model for Footer Left section.
 *
 * This model represents the left side of the footer, including
 * logo, contact details, and navigation links.
 *
 * Data Source:
 * - Reads properties from component resource
 * - Reads child resources under "linksColumn1" and "linksColumn2"
 *
 * Responsibilities:
 * - Provide branding and contact details
 * - Provide navigation links grouped into columns
 *
 * Injection Strategy:
 * Uses OPTIONAL injection to handle missing data gracefully.
 */
@Model(
        adaptables = Resource.class,
        adapters = FooterLeftModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterLeftModel {

    /**
     * Logo image path.
     */
    @ValueMapValue
    private String logo;

    /**
     * Address text.
     */
    @ValueMapValue
    private String address;

    /**
     * Contact phone number.
     */
    @ValueMapValue
    private String phone;

    /**
     * Contact email address.
     */
    @ValueMapValue
    private String email;

    /**
     * First column of footer links.
     */
    @ChildResource(name = "linksColumn1")
    private List<Link> linksColumn1;

    /**
     * Second column of footer links.
     */
    @ChildResource(name = "linksColumn2")
    private List<Link> linksColumn2;

    public String getLogo() {
        return logo;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<Link> getLinksColumn1() {
        return linksColumn1;
    }

    public List<Link> getLinksColumn2() {
        return linksColumn2;
    }

    /**
     * Inner model representing a footer link.
     *
     * Each link corresponds to a child resource.
     */
    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class Link {

        /**
         * Display text of the link.
         */
        @ValueMapValue
        private String text;

        /**
         * URL of the link.
         */
        @ValueMapValue
        private String url;

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }
}