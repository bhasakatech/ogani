package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = FooterLeftModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterLeftModel {

    @ValueMapValue
    private String logo;

    @ValueMapValue
    private String address;

    @ValueMapValue
    private String phone;

    @ValueMapValue
    private String email;

    @ChildResource(name = "linksColumn1")
    private List<Link> linksColumn1;

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


    @Model(adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class Link {

        @ValueMapValue
        private String text;

        @ValueMapValue
        private String url;


        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }}