package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ContactItem {
    @ValueMapValue
    private String icon;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String value;

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
