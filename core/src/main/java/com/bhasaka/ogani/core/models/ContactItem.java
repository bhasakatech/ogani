package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
/**
 * Sling model representing a single contact detail item.
 */
public class ContactItem {
    @ValueMapValue
    private String icon;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String value;

    /**
     * Returns the icon class or identifier configured for this contact item.
     *
     * @return the icon value
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns the label/title of the contact item.
     *
     * @return the title value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the contact detail content (for example, phone or email).
     *
     * @return the contact value
     */
    public String getValue() {
        return value;
    }
}
