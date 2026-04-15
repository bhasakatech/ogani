package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.Collections;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
/**
 * Sling model for the contact section component.
 * @author Chandraprakash
 * <p>Exposes the configured list of contact items and returns an empty list
 * when no child resources are present.</p>
 */
public class ContactSectionModel {

    @ChildResource
    private List<ContactItem> contactItems;

    /**
     * Returns the configured contact items.
     *
     * @return contact items, or an empty list when none are configured
     */
    public List<ContactItem> getContactItems() {

        return contactItems != null ? contactItems : Collections.emptyList();
    }


}
